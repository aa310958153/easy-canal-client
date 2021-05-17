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

    private Map<String, ProcessListener> listenerMap = new HashMap<String, ProcessListener>();
    public void register(Map<String, ProcessListener> processListenerMap) throws Exception {
        for (ProcessListener processListener :
                processListenerMap.values()) {
            register(processListener);
        }
    }
    public void register(ProcessListener processListener){
        Table[] table = processListener.getClass().getAnnotationsByType(Table.class);
        if (table!=null&&table.length>0) {
            listenerMap.put(table[0].name(), processListener);
        }
    }

    public Set<String> getTables(){
       return listenerMap.keySet();
    }


    @Override
    public ProcessListener routing(String tableName) {
        return listenerMap.get(tableName);
    }
}
