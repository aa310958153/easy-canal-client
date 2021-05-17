package com.easy.canal.test.listener;

import com.alibaba.fastjson.JSON;
import com.easy.canal.test.entity.PaymentWayAndRelation;
import com.wine.easy.canal.annotation.Table;
import com.wine.easy.canal.interfaces.ProcessListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Project easy-canal-parent
 * @PackageName com.easy.canal.test
 * @ClassName PaymentWayAndRelation
 * @Author qiang.li
 * @Date 2021/4/8 4:18 下午
 * @Description TODO
 */
@Table(name = "merge_test.ord_pay_way")
@Component
public class PaymentWayAndRelationListener implements ProcessListener<PaymentWayAndRelation> {
    private static final Logger logger = LoggerFactory.getLogger(PaymentWayAndRelationListener.class);
    @Override
    public void update(PaymentWayAndRelation after, PaymentWayAndRelation before, List<String> updateFiled) {
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
}
