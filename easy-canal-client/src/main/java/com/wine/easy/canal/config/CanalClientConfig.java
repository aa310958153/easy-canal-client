package com.wine.easy.canal.config;

import com.wine.easy.canal.core.ListenerMethodArgumentResolver;
import com.wine.easy.canal.yaml.config.YamlConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.core
 * @ClassName CanalInfoConfig
 * @Author qiang.li
 * @Date 2021/3/24 12:33 下午
 * @Description 用于config配置
 */
@Configuration
public class  CanalClientConfig  extends YamlConfiguration {
    private String hosts;
    private String zkHosts;
    private Integer batchSize;
    private String username;
    private String password;
    private Map<String,GroupConfig> groups;
    private Map<String,String> properties;
    //对于请求结果的转换器
    public static ListenerMethodArgumentResolver LISTENERMETHODARGUMENTRESOLVER = new ListenerMethodArgumentResolver();

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getZkHosts() {
        return zkHosts;
    }

    public void setZkHosts(String zkHosts) {
        this.zkHosts = zkHosts;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, GroupConfig> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, GroupConfig> groups) {
        this.groups = groups;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

}
