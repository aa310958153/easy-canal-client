package com.wine.easy.canal.config;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.config
 * @ClassName GroupConfig
 * @Author qiang.li
 * @Date 2021/5/31 10:13 上午
 * @Description TODO
 */
public class GroupConfig {
    private Integer batchSize;
    private String  destination;
    private String name;

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
