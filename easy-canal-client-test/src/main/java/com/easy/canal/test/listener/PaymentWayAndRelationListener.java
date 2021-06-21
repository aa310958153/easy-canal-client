package com.easy.canal.test.listener;

import com.alibaba.fastjson.JSON;
import com.easy.canal.test.entity.PaymentWayAndRelation;
import com.wine.easy.canal.annotation.Table;
import com.wine.easy.canal.config.CanalClientConfig;
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
 * @ClassName PaymentWayAndRelation
 * @Author qiang.li
 * @Date 2021/4/8 4:18 下午
 */
@Table(name = "${database}.ord_pay_way",group = "g1")
@Component
public class PaymentWayAndRelationListener implements ProcessListener<PaymentWayAndRelation> {
    private static final Logger logger = LoggerFactory.getLogger(PaymentWayAndRelationListener.class);
    @Override
    public void update(PaymentWayAndRelation after, PaymentWayAndRelation before, Set<String> updateFiled) {
        logger.info("-------------------------触发修改-------------------------");
        logger.info("修改一条数据修改后:{}", JSON.toJSON(after));
        logger.info("修改一条数据修改前:{}", JSON.toJSON(before));
        logger.info("修改一条数据修改字段:{}", JSON.toJSON(updateFiled));
        logger.info("-------------------------触发end-------------------------");
    }

    @Override
    public void insert(PaymentWayAndRelation paymentWayAndRelation) {
        logger.info("新增一条数据{}", JSON.toJSON(paymentWayAndRelation));
    }
    @Override
    public void delete(PaymentWayAndRelation paymentWayAndRelation) {
        logger.info("删除一条数据{}", JSON.toJSON(paymentWayAndRelation));
    }
    /**
     * 可以根据指定条件全量 如: where create_time>='2020-01-02'
     * @param datas
     */
    @Override
    public void elk(List<PaymentWayAndRelation> datas) {
        for(PaymentWayAndRelation paymentWayAndRelation:datas) {
            logger.info("elk{}", JSON.toJSON(paymentWayAndRelation));
        }
    }
    /**
     * 处理失败回调。不阻塞后续返回true则跳过,返回false会不断重试
     * @param entry
     */
    @Override
    public boolean errorCallback(Dml entry) {
       //记录日志后续补偿
        return true;
    }
}
