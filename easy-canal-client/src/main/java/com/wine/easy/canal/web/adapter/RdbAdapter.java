package com.wine.easy.canal.web.adapter;

import com.google.common.base.Joiner;
import com.google.protobuf.InvalidProtocolBufferException;
import com.wine.easy.canal.config.CanalClientConfig;
import com.wine.easy.canal.config.DbMapping;
import com.wine.easy.canal.config.ELKConfig;
import com.wine.easy.canal.core.EditMetaInfo;
import com.wine.easy.canal.exception.ReflectionException;
import com.wine.easy.canal.interfaces.ProcessListener;
import com.wine.easy.canal.interfaces.Register;
import com.wine.easy.canal.web.entity.EtlResult;
import com.wine.easy.canal.yaml.CanalClientConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSetMetaData;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.web.adapter
 * @ClassName RdbAdapter
 * @Author qiang.li
 * @Date 2021/6/1 5:06 下午
 * @Description TODO
 */
public class RdbAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RdbAdapter.class);
    private ELKConfig elkConfig;
    private static final int CNT_PER_TASK=10000;
    private Register register;
    public RdbAdapter(Register register,ELKConfig elkConfig){
        this.register=register;
        this.elkConfig=elkConfig;
    }

    public void init() throws IOException {

    }

    public EtlResult etl(String task, String conditions, List<String> params) {
        DbMapping dbMapping= elkConfig.getDbMappings().get(task);
        Map<String, ?> jdbc= dbMapping.getJdbc();
        if(jdbc==null){
            jdbc=elkConfig.getJdbc();
        }
        DataSource dataSource=DataSourceUtil.initSingeDataSource(jdbc);
        String sql = "SELECT * FROM " + dbMapping.getTable();
        return importData(sql,conditions,params,dataSource,dbMapping);
    }
    public EtlResult importData(String sql,String condition,List<String> params,DataSource dataSource,DbMapping dbMapping){
        ProcessListener processListener=register.routingListener(dbMapping.getGroup(),dbMapping.getTable());
        EtlResult etlResult = new EtlResult();
        //计数器
        AtomicLong impCount = new AtomicLong();
        List<String> errMsg = new ArrayList<>();
        if (dataSource == null) {
            logger.warn("dataSource is null, etl go end ");
            etlResult.setErrorMessage("dataSource is null, etl go end ");
            return etlResult;
        }

        long start = System.currentTimeMillis();
        try {
            List<Object> values = new ArrayList<>();
            // 拼接条件
            if (condition != null && params != null) {
                String etlCondition =condition;
                for (String param : params) {
                    etlCondition = etlCondition.replace("{}", "?");
                    values.add(param);
                }
                sql += " " + etlCondition;
            }

            if (logger.isDebugEnabled()) {
                logger.debug("etl sql : {}", sql);
            }

            // 获取总数
            String countSql = "SELECT COUNT(1) FROM ( " + sql + ") _CNT ";
            long cnt = (Long) Util.sqlRS(dataSource, countSql, values, rs -> {
                Long count = null;
                try {
                    if (rs.next()) {
                        count = ((Number) rs.getObject(1)).longValue();
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                return count == null ? 0L : count;
            });

            // 当大于1万条记录时开启多线程
            if (cnt >= 10000) {
                //获取当前机器核数
                int threadCount = Runtime.getRuntime().availableProcessors();

                long offset;
                //10000为一个包 但不是一个批次
                long size = CNT_PER_TASK;
                //根据数量除每个线程处理10000
                long workerCnt = cnt / size + (cnt % size == 0 ? 0 : 1);

                if (logger.isDebugEnabled()) {
                    logger.debug("workerCnt {} for cnt {} threadCount {}", workerCnt, cnt, threadCount);
                }
                //初始化固定线池
                ExecutorService executor = Util.newFixedThreadPool(threadCount, 5000L);
                List<Future<Boolean>> futures = new ArrayList<>();
                for (long i = 0; i < workerCnt; i++) {
                    offset = size * i;
                    String sqlFinal = sql + " LIMIT " + offset + "," + size;
                    Future<Boolean> future = executor.submit(() -> executeSqlImport(dataSource,
                            sqlFinal,
                           values,
                           dbMapping,
                            processListener,
                            impCount,
                            errMsg));
                    futures.add(future);
                }

                for (Future<Boolean> future : futures) {
                    future.get();
                }
                executor.shutdown();
            } else {
                executeSqlImport(dataSource, sql,values,dbMapping,processListener, impCount, errMsg);
            }

            logger.info("数据全量导入完成, 一共导入 {} 条数据, 耗时: {}", impCount.get(), System.currentTimeMillis() - start);
            etlResult.setResultMessage("导入 数据：" + impCount.get() + " 条");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            errMsg.add(" 数据导入异常 =>" + e.getMessage());
        }
        if (errMsg.isEmpty()) {
            etlResult.setSucceeded(true);
        } else {
            etlResult.setErrorMessage(Joiner.on("\n").join(errMsg));
        }
        return etlResult;
    }

    /**
     * 执行导入
     */
    protected boolean executeSqlImport(DataSource srcDS, String sql, List<Object> values,
                                       DbMapping dbMapping, ProcessListener processListener, AtomicLong impCount, List<String> errMsg) {
        try {
            List<String> columns = new ArrayList<>();
            Util.sqlRS(srcDS, "SELECT * FROM " +dbMapping.getTable()  + " LIMIT 1 ", rs -> {
                try {
                    ResultSetMetaData rsd = rs.getMetaData();
                    int columnCount = rsd.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        columns.add(rsd.getColumnName(i));
                    }
                    return true;
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return false;
                }
            });

            Util.sqlRS(srcDS, sql,values, rs -> {
                int idx = 1;

                try {
                    List<Map<String,Object>> rows=new ArrayList<>();
                    boolean completed = false;
                    while (rs.next()) {
                        Map<String,Object> row=new HashMap<>();
                        for (String column :
                                columns) {
                            Object value = rs.getObject(column);
                            row.put(column,value);
                        }
                        rows.add(row);
                        if (idx % dbMapping.getCommitBatch() == 0) {
                            push(rows,processListener);
                            completed = true;
                        }
                        idx++;
                        impCount.incrementAndGet();

                    }
                    if (!completed) {
                        push(rows,processListener);
                    }


                } catch (Exception e) {
                    logger.error(dbMapping.getTable() + " etl failed! ==>" + e.getMessage(), e);
                    errMsg.add(dbMapping.getTable() + " etl failed! ==>" + e.getMessage());
                }
                return idx;
            });
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public void  push(List<Map<String,Object>> rows,ProcessListener processListener) throws InvalidProtocolBufferException, ReflectionException, IllegalAccessException, ParseException, InvocationTargetException {
        if(rows!=null&&rows.size()>0) {
            List<EditMetaInfo> editMetaInfos = CanalClientConfig.LISTENERMETHODARGUMENTRESOLVER.resolver(processListener, rows);
            rows.clear();
            List datas=new ArrayList();
            for (EditMetaInfo editMetaInfo:
                    editMetaInfos ) {
                datas.add(editMetaInfo.getAfter());
            }
            editMetaInfos.clear();
            processListener.elk(datas);
        }
    }
}
