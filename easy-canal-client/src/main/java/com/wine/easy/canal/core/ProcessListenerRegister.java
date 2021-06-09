package com.wine.easy.canal.core;

import com.wine.easy.canal.annotation.Table;
import com.wine.easy.canal.interfaces.ProcessListener;
import com.wine.easy.canal.interfaces.Register;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Project easy-canal
 * @PackageName com.wine.easy.canal.core
 * @ClassName ProcessListenerRegister
 * @Author qiang.li
 * @Date 2021/3/23 6:07 下午
 * @Description 用于封装注册所有的监听器
 */
public class ProcessListenerRegister implements Register {
    private Map<String, ProcessListener> tabMappingListenerMap = new HashMap<String, ProcessListener>();
    private Map<String, List<ProcessListener>> groupMappingListenerMap = new HashMap<String, List<ProcessListener>>();
    private Map<String, Set<String>> groupTableMap = new HashMap<String, Set<String>>();
    public void register(Map<String, ProcessListener> processListenerMap) throws Exception {
        for (ProcessListener processListener :
                processListenerMap.values()) {
            register(processListener);
        }
    }
    public void register(ProcessListener processListener){
        Table[] table = processListener.getClass().getAnnotationsByType(Table.class);
        if (table==null&&table.length<=0) {
           return;
        }
        tabMappingListenerMap.put(String.format("%s_%s",table[0].group(),table[0].name()),processListener);
        String group= table[0].group();
        if(groupMappingListenerMap.get(group)==null){
            groupMappingListenerMap.put(group,new ArrayList<>());
        }
        groupMappingListenerMap.get(table[0].group()).add(processListener);;

        if(groupTableMap.get(group)==null){
            groupTableMap.put(group,new HashSet<>());
        }
        groupTableMap.get(table[0].group()).add(table[0].name());;
    }



    public Set<String> getGroups(){
        return groupMappingListenerMap.keySet();
    }


    @Override
    public ProcessListener routingListener(String group,String tableName) {
        return tabMappingListenerMap.get(String.format("%s_%s",group,tableName));
    }

    @Override
    public Set<String> getTablesByGroup(String group) {
        return groupTableMap.get(group);
    }
}
