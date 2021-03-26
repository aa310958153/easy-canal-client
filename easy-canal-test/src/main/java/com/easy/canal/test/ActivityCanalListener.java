package com.easy.canal.test;

import com.alibaba.fastjson.JSON;
import com.wine.easy.canal.annotation.Table;
import com.wine.easy.canal.core.CanalListenerWorker;
import com.wine.easy.canal.interfaces.ProcessListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Project easy-canal-parent
 * @PackageName com.easy.canal.test
 * @ClassName ActivityCanalListenerWorker
 * @Author qiang.li
 * @Date 2021/3/24 12:42 下午
 * @Description TODO
 */

@Table(name = "canal.act_activity")
@Component
public class ActivityCanalListener implements ProcessListener<Activity> {
    private static final Logger logger = LoggerFactory.getLogger(ActivityCanalListener.class);

    @Override
    public void update(Activity after, Activity before, List<String> updateFiled) {
        logger.info("-------------------------触发修改-------------------------");
        logger.info("修改一条数据修改前:{}", JSON.toJSON(after));
        logger.info("修改一条数据修改后:{}", JSON.toJSON(before));
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
}
