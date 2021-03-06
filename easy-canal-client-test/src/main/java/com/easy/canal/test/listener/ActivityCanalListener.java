package com.easy.canal.test.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.easy.canal.test.entity.Activity;
import com.wine.easy.canal.annotation.Table;
import com.wine.easy.canal.interfaces.ProcessListener;
import com.wine.easy.canal.tool.Dml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @Project easy-canal-parent
 * @PackageName com.easy.canal.test
 * @ClassName ActivityCanalListenerWorker
 * @Author qiang.li
 * @Date 2021/3/24 12:42 下午
 * @Description TODO
 */

@Table(name = "canal.act_activity",group = "g2")
@Component
public class ActivityCanalListener implements ProcessListener<Activity> {
    private static final Logger logger = LoggerFactory.getLogger(ActivityCanalListener.class);

    @Override
    public void update(Activity after, Activity before, Set<String> updateFiled) {
        logger.info("-------------------------触发修改-------------------------");
        logger.info("修改一条数据修改后:{}", JSON.toJSON(after));
        logger.info("修改一条数据修改前:{}", JSON.toJSON(before));
        logger.info("修改一条数据修改字段:{}", JSON.toJSON(updateFiled));
        logger.info("-------------------------触发end-------------------------");
    }

    @Override
    public void insert(Activity activity) {
        logger.info("新增一条数据{}", JSON.toJSON(activity));
    }

    @Override
    public void delete(Activity activity) {
        logger.info("删除一条数据{}", JSON.toJSON(activity));
    }

    /**
     * 理失败回调。不阻塞后续返回true则跳过,返回false会不断重试 但是会阻塞后续binlog 直到成功
     *
     * @param entry
     * @param ex
     * @return
     */
    @Override
    public boolean errorCallback(Dml entry, Exception ex) {
        logger.error("出现异常:dml:{}",JSON.toJSONString(entry),ex);
        return true;
    }


}
