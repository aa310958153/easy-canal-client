package com.easy.canal.test.listener;

import com.alibaba.fastjson.JSON;
import com.easy.canal.test.entity.Classes;
import com.wine.easy.canal.annotation.Table;
import com.wine.easy.canal.interfaces.ProcessListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Project easy-canal-parent
 * @PackageName com.easy.canal.test
 * @ClassName ClassesListener
 * @Author qiang.li
 * @Date 2021/3/25 3:44 下午
 * @Description TODO
 */
@Table(name = "canal.classes")
@Component
public class ClassesListener implements ProcessListener<Classes> {
    private static final Logger logger = LoggerFactory.getLogger(ClassesListener.class);

    @Override
    public void update(Classes after, Classes before, List<String> updateFiled) {
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
}
