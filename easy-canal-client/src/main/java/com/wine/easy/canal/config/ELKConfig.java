package com.wine.easy.canal.config;

import com.wine.easy.canal.yaml.config.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.config
 * @ClassName ELKConfig
 * @Author qiang.li
 * @Date 2021/6/1 4:59 下午
 * @Description TODO
 */
public class ELKConfig  extends YamlConfiguration {
    private Map<String,?> jdbc;
    private Map<String,DbMapping> dbMappings;
    private int port;

    public Map<String, ?> getJdbc() {
        return jdbc;
    }

    public void setJdbc(Map<String, ?> jdbc) {
        this.jdbc = jdbc;
    }

    public Map<String, DbMapping> getDbMappings() {
        return dbMappings;
    }

    public void setDbMappings(Map<String, DbMapping> dbMappings) {
        this.dbMappings = dbMappings;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
