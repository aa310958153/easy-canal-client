package com.easy.canal.test.listener;

import com.alibaba.fastjson.JSON;
import com.easy.canal.test.entity.Classes;
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
 * @ClassName ClassesListener
 * @Author qiang.li
 * @Date 2021/3/25 3:44 下午
 * @Description TODO
 */
@Table(name = "canal.classes",group = "g3")
@Component
public class ClassesListener implements ProcessListener<Classes> {
    private static final Logger logger = LoggerFactory.getLogger(ClassesListener.class);

    @Override
    public void update(Classes after, Classes before, Set<String> updateFiled) {
        logger.info("-------------------------触发修改-------------------------");
        logger.info("修改一条数据修改后:{}", JSON.toJSON(after));
        logger.info("修改一条数据修改前:{}", JSON.toJSON(before));
        logger.info("修改一条数据修改字段:{}", JSON.toJSON(updateFiled));
        logger.info("-------------------------触发end-------------------------");
    }

    @Override
    public void insert(Classes classes) {
        logger.info("新增一条数据{}", JSON.toJSON(classes));
    }

    @Override
    public void delete(Classes classes) {
        logger.info("删除一条数据{}", JSON.toJSON(classes));
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
