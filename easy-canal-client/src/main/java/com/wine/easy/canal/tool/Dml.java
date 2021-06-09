package com.wine.easy.canal.tool;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.tool
 * @ClassName Dml
 * @Author qiang.li
 * @Date 2021/5/31 11:30 上午
 * @Description TODO
 */

public class Dml implements Serializable {

    private String                    destination;                            // 对应canal的实例或者MQ的topic
    private String                    groupId;                                //
    private String                    database;                               // 数据库或schema
    private String                    table;                                  // 表名
    private List<String> pkNames;
    private Boolean                   isDdl;
    private String                    type;                                   // 类型: INSERT UPDATE DELETE
    // binlog executeTime
    private Long                      es;                                     // 执行耗时
    // dml build timeStamp
    private Long                      ts;                                     // 同步时间
    private String                    sql;                                    // 执行的sql, dml sql为空
    private List<Row> data;                                   // 数据列表
    private Set<String> updatedNames;

    public static class Row{
        Map<String, Object> data;
        Map<String, Object> old;

        public Map<String, Object> getData() {
            return data;
        }

        public void setData(Map<String, Object> data) {
            this.data = data;
        }

        public Map<String, Object> getOld() {
            return old;
        }

        public void setOld(Map<String, Object> old) {
            this.old = old;
        }
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<String> getPkNames() {
        return pkNames;
    }

    public void setPkNames(List<String> pkNames) {
        this.pkNames = pkNames;
    }

    public Boolean getDdl() {
        return isDdl;
    }

    public void setDdl(Boolean ddl) {
        isDdl = ddl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getEs() {
        return es;
    }

    public Set<String> getUpdatedNames() {
        return updatedNames;
    }

    public void setUpdatedNames(Set<String> updatedNames) {
        this.updatedNames = updatedNames;
    }

    public void setEs(Long es) {
        this.es = es;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Row> getData() {
        return data;
    }

    public void setData(List<Row> data) {
        this.data = data;
    }


// 旧数据列表, 用于update, size和data的size一一对应
}
