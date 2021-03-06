package com.wine.easy.canal.interfaces;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.wine.easy.canal.core.CanalListenerWorker;
import com.wine.easy.canal.tool.Dml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project easy-canal
 * @PackageName com.wine.easy.canal.annotation
 * @ClassName Listener
 * @Author qiang.li
 * @Date 2021/3/23 6:01 下午
 * @Description TODO
 */
public interface ProcessListener<T> {
    public void update(T after, T before, Set<String> updateFields);
    public void insert(T t);
    public void delete(T t);
    static final Logger logger = LoggerFactory.getLogger(CanalListenerWorker.class);

    /**
     * 理失败回调。不阻塞后续返回true则跳过,返回false会不断重试 但是会阻塞后续binlog 直到成功
     * @param entry
     * @return
     */
     boolean errorCallback(Dml entry,Exception ex);

    /**
     * elk用于全量或者增量
     * @param datas
     */
    default  void elk(List<T> datas){
        
    }
}
