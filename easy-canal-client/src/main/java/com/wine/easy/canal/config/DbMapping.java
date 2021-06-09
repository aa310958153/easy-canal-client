package com.wine.easy.canal.config;

import java.util.Map;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.config
 * @ClassName DbMapping
 * @Author qiang.li
 * @Date 2021/6/2 1:05 下午
 * @Description TODO
 */
public class DbMapping {
    private String table;
    private String group;
    private String condition;
    private Map<String,?> jdbc;
    private Integer commitBatch=100;

    public String getTable() {
        return table;
    }

    public Map<String, ?> getJdbc() {
        return jdbc;
    }

    public void setJdbc(Map<String, ?> jdbc) {
        this.jdbc = jdbc;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getCommitBatch() {
        return commitBatch;
    }

    public void setCommitBatch(Integer commitBatch) {
        this.commitBatch = commitBatch;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
