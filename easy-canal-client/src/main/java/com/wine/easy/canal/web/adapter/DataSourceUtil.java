package com.wine.easy.canal.web.adapter;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.web.adapter
 * @ClassName DatSourceUtil
 * @Author qiang.li
 * @Date 2021/6/2 1:13 下午
 * @Description TODO
 */
public class DataSourceUtil {
    private static final Logger log = LoggerFactory.getLogger(DataSourceUtil.class);
    private static Map<String, DataSource> dataSourceMap=new HashMap<>();
    public static DataSource  initSingeDataSource(Map<String,?> jdbc){
        String key=(String)jdbc.get("url");
        DataSource dataSource=dataSourceMap.get(key);
        if(dataSource==null){
            DruidDataSource  druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName((String)jdbc.get("driverClassName"));
            druidDataSource.setUrl((String)jdbc.get("url"));
            druidDataSource.setUsername((String)jdbc.get("username"));
            druidDataSource.setPassword((String) jdbc.get("password"));
            druidDataSource.setInitialSize(1);
            druidDataSource.setMinIdle(1);
            druidDataSource.setMaxActive(30);
            druidDataSource.setMaxWait(60000);
            druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
            druidDataSource.setMinEvictableIdleTimeMillis(300000);
            druidDataSource.setUseUnfairLock(true);
            //liqiangtodo 针对utf8mb4 需要把此配置打开
            List<String> array = new ArrayList<>();
            array.add("set names utf8mb4;");
            druidDataSource.setConnectionInitSqls(array);

            try {
                druidDataSource.init();
                dataSourceMap.put(key,druidDataSource);
                dataSource=druidDataSource;
            } catch (SQLException e) {
                log.error("ERROR ## failed to initial datasource: " +(String) jdbc.get("jdbc.url"), e);
            }
        }
        return dataSource;
    }
}
