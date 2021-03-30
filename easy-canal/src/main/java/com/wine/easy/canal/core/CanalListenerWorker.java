package com.wine.easy.canal.core;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;
import com.wine.easy.canal.exception.ReflectionException;
import com.wine.easy.canal.interfaces.ProcessListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Project easy-canal
 * @PackageName com.wine.easy.canal.core
 * @ClassName CanalListenerWorker
 * @Author qiang.li
 * @Date 2021/3/24 10:11 上午
 * @Description TODO
 */
public class CanalListenerWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CanalListenerWorker.class);
    private ListenerMethodArgumentResolver listenerMethodArgumentResolver = new ListenerMethodArgumentResolver();
    private CanalContext canalContext;
    private CanalConnector canalConnector;

    public CanalListenerWorker(CanalContext CanalContext) {
        this.canalContext = CanalContext;
    }

    public void start() {
        canalConnector();
        startCanalListener();
    }

    public void startCanalListener() {
        new Thread(this).start();
        logger.info("开始监听canal.......");
    }

    public void end() {
        if (canalConnector != null) {
            canalConnector.disconnect();
            canalConnector=null;
        }
    }

    public void canalConnector() {
        canalConnector = ConnectionFactory.create(canalContext.getCanalInfoConfig());
        canalConnector.connect();
        //这个方法的意思就是在jvm中增加一个关闭的钩子，当jvm关闭的时候，会执行系统中已经设置的所有通过方法addShutdownHook添加的钩子，当系统执行完这些钩子后，jvm才会关闭。所以这些钩子可以在jvm关闭的时候进行内存清理、对象销毁等操作。
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                logger.info("## stop the canal client");
                 this.end();
            } catch (Throwable e) {
                logger.warn("##something goes wrong when stopping canal:", e);
            } finally {
                logger.info("## canal client is down.");
            }
        }));
        // 指定filter 监听的表 具体看文档
        Set<String> tables=canalContext.getRegister().getTables();
        String rule=tables.stream()
                .map(color -> color.toString())
                .collect(Collectors.joining(","));
        canalConnector.subscribe(rule);
        // 回滚寻找上次中断的位置
        canalConnector.rollback();
        logger.info("canal客户端启动成功.......");
    }

    @Override
    public void run() {
        int emptyCount = 0;
        int batchSize = canalContext.getCanalInfoConfig().getBatchSize();
        while (true) {
            try {
//            Message message = connector.get(batchSize);
                Message message = canalConnector.getWithoutAck(batchSize);
                long batchId = message.getId();
                try {
                    List<CanalEntry.Entry> entries = message.getEntries();
                    if (batchId != -1 && entries.size() > 0) {
                        for (CanalEntry.Entry entry :
                                entries) {
                            if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA) {
                                String database = entry.getHeader().getSchemaName();
                                String table = entry.getHeader().getTableName();
                                String key = String.format("%s.%s", database, table);
                                ProcessListener<?> processListener = canalContext.getRegister().routing(key);
                                if (processListener == null) {
                                    logger.warn("未找到对应的处理器key:{}", key);
                                    continue;
                                }
                                process(processListener, entry);
                            }
                        }

                    } else {
                        emptyCount++;
                        Thread.yield();
                        if (emptyCount > 10) {
                            emptyCount = 0;
                            sleep(1000);
                        }
                    }
                    canalConnector.ack(batchId);
                } catch (Exception e) {
                    sleep(1000);
                    logger.error("发送监听事件失败！batchId回滚,batchId=" + batchId, e);
                    canalConnector.rollback(batchId);
                }
            } catch (Exception e) {
                sleep(1000);
                logger.error("canal_scheduled异常！", e);
            }
        }
    }

    public void process(ProcessListener processListener, CanalEntry.Entry entry) throws InvalidProtocolBufferException, ReflectionException, InvocationTargetException, IllegalAccessException, ParseException {
        CanalEntry.EventType eventType = entry.getHeader().getEventType();
        List<EditInfo> arguments = listenerMethodArgumentResolver.resolver(processListener, entry);;
        switch (eventType) {
            case INSERT:

                for (EditInfo argument :
                        arguments) {
                    processListener.insert(argument.getAfter());
                }
                break;
            case UPDATE:
                for (EditInfo argument :
                        arguments) {
                    processListener.update(argument.getAfter(), argument.getBefore(),argument.getUpdatedProperty());
                }

                break;
            case DELETE:
                for (EditInfo argument :
                        arguments) {
                    processListener.delete(argument.getBefore());
                }
                break;
            default:
                break;
        }

    }

    public void sleep(Integer second) {
        try {
            Thread.sleep(second);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
}
