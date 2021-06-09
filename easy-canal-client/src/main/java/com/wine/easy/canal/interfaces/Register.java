package com.wine.easy.canal.interfaces;

import java.util.Map;
import java.util.Set;

/**
 * @Project easy-canal
 * @PackageName com.wine.easy.canal.interfaces
 * @ClassName Register
 * @Author qiang.li
 * @Date 2021/3/24 10:34 上午
 * @Description TODO
 */
public interface Register {
    public void register(Map<String, ProcessListener> processListenerMap) throws Exception;
    public void register(ProcessListener processListener);
    public ProcessListener routingListener(String group,String tableName);
    public Set<String> getTablesByGroup(String destination);
    public Set<String> getGroups();
}
