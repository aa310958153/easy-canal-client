package com.wine.easy.canal.core;

import com.wine.easy.canal.annotation.Table;
import com.wine.easy.canal.config.CanalClientConfig;
import com.wine.easy.canal.interfaces.ProcessListener;
import com.wine.easy.canal.interfaces.Register;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private ApplicationContext applicationContext;
    private CanalClientConfig canalClientConfig;
    private Map<String, ProcessListener> tabMappingListenerMap = new HashMap<String, ProcessListener>();
    private Map<String, List<ProcessListener>> groupMappingListenerMap = new HashMap<String, List<ProcessListener>>();
    private Map<String, Set<String>> groupTableMap = new HashMap<String, Set<String>>();
    public ProcessListenerRegister(ApplicationContext applicationContext,CanalClientConfig canalClientConfig){
        this.applicationContext=applicationContext;
        this.canalClientConfig=canalClientConfig;
    }
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
        String tableName=getParseValue(table[0].name());
        tabMappingListenerMap.put(String.format("%s_%s",table[0].group(),tableName),processListener);
        String group= table[0].group();
        if(groupMappingListenerMap.get(group)==null){
            groupMappingListenerMap.put(group,new ArrayList<>());
        }
        groupMappingListenerMap.get(table[0].group()).add(processListener);;

        if(groupTableMap.get(group)==null){
            groupTableMap.put(group,new HashSet<>());
        }
        groupTableMap.get(table[0].group()).add(tableName);;
    }
    public String getParseValue(String value){
        Pattern p = Pattern.compile("\\$\\{(?<data>[^\\{]+)\\}");
        Matcher matcher=  p.matcher(value);
        StringBuffer sr = new StringBuffer();
        while (matcher.find()){
          String propertiesValue=null;
          String propertiesName=  matcher.group(1);
          if(canalClientConfig.getProperties()!=null){
              propertiesValue=canalClientConfig.getProperties().get(propertiesName);
          }
          if(propertiesValue==null){
              propertiesValue=applicationContext.getEnvironment().getProperty(propertiesName);
          }
          if(propertiesValue!=null){
              matcher.appendReplacement(sr,propertiesValue);
          }
              matcher.appendTail(sr);
        }
        return sr.length()>0?sr.toString():value;
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
