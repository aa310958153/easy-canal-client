package com.wine.easy.canal.tool;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.tool
 * @ClassName MessageUtil
 * @Author qiang.li
 * @Date 2021/5/31 11:31 上午
 * @Description TODO
 */
public class MessageUtil {
    public static List<Dml> parse4Dml(String destination, String groupId, Message message) {
        if (message == null) {
            return null;
        }
        List<CanalEntry.Entry> entries = message.getEntries();
        List<Dml> dmls = new ArrayList<Dml>(entries.size());
        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN
                    || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }
            CanalEntry.RowChange rowChange;
            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }
            CanalEntry.EventType eventType = rowChange.getEventType();
            final Dml dml = new Dml();
            dml.setDdl(rowChange.getIsDdl());
            dml.setDestination(destination);
            dml.setGroupId(groupId);
            dml.setDatabase(entry.getHeader().getSchemaName());
            dml.setTable(entry.getHeader().getTableName());
            dml.setType(eventType.toString());
            dml.setEs(entry.getHeader().getExecuteTime());
            dml.setTs(System.currentTimeMillis());
            dml.setSql(rowChange.getSql());
            dmls.add(dml);
            if (!rowChange.getIsDdl()) {
                List<Dml.Row> rows=new ArrayList<>();
                Set<String> updateSet = new HashSet<>();
                dml.setPkNames(new ArrayList<>());
                int i = 0;
                for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                    if (eventType != CanalEntry.EventType.INSERT && eventType != CanalEntry.EventType.UPDATE
                            && eventType != CanalEntry.EventType.DELETE) {
                        continue;
                    }
                    Dml.Row dmlRow=new Dml.Row();
                    Map<String, Object> row = new LinkedHashMap<>();
                    List<CanalEntry.Column> columns;

                    if (eventType == CanalEntry.EventType.DELETE) {
                        columns = rowData.getBeforeColumnsList();
                    } else {
                        columns = rowData.getAfterColumnsList();
                    }

                    for (CanalEntry.Column column : columns) {
                        if (i == 0) {
                            if (column.getIsKey()) {
                                dml.getPkNames().add(column.getName());
                            }
                        }
                        if (column.getIsNull()) {
                            row.put(column.getName(), null);
                        } else {
//                            row.put(column.getName(),
//                                    JdbcTypeUtil.typeConvert(dml.getTable(),
//                                            column.getName(),
//                                            column.getValue(),
//                                            column.getSqlType(),
//                                            column.getMysqlType()));
                            row.put(column.getName(),
                                    column.getValue());
                        }
                        // 获取update为true的字段
                        if (column.getUpdated()) {
                            updateSet.add(column.getName());
                        }
                    }
                    if (!row.isEmpty()) {
                        dmlRow.setData(row);
                    }
                    if (eventType == CanalEntry.EventType.UPDATE) {
                        Map<String, Object> rowOld = new LinkedHashMap<>();
                        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
                                if (column.getIsNull()) {
                                    rowOld.put(column.getName(), null);
                                } else {
//                                    rowOld.put(column.getName(),
//                                            JdbcTypeUtil.typeConvert(dml.getTable(),
//                                                    column.getName(),
//                                                    column.getValue(),
//                                                    column.getSqlType(),
//                                                    column.getMysqlType()));
                                    rowOld.put(column.getName(),
                                            column.getValue());
                                }
                        }
                        // update操作将记录修改前的值
                        if (!rowOld.isEmpty()) {
                            dmlRow.setOld(rowOld);
                        }
                    }
                    if(!CollectionUtils.isEmpty(dmlRow.getData())||!CollectionUtils.isEmpty(dmlRow.getOld())){
                        rows.add(dmlRow);
                    }
                    i++;
                }
                dml.setData(rows);
                dml.setUpdatedNames(updateSet);
            }
        }

        return dmls;
    }



    private static List<Map<String, Object>> changeRows(String table, List<Map<String, String>> rows,
                                                        Map<String, Integer> sqlTypes, Map<String, String> mysqlTypes) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, String> row : rows) {
            Map<String, Object> resultRow = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : row.entrySet()) {
                String columnName = entry.getKey();
                String columnValue = entry.getValue();

                Integer sqlType = sqlTypes.get(columnName);
                if (sqlType == null) {
                    continue;
                }

                String mysqlType = mysqlTypes.get(columnName);
                if (mysqlType == null) {
                    continue;
                }

                Object finalValue = JdbcTypeUtil.typeConvert(table, columnName, columnValue, sqlType, mysqlType);
                resultRow.put(columnName, finalValue);
            }
            result.add(resultRow);
        }
        return result;
    }
}
