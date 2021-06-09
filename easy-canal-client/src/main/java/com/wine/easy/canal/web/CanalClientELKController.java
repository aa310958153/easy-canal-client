package com.wine.easy.canal.web;

import com.wine.easy.canal.config.ELKConfig;
import com.wine.easy.canal.interfaces.Register;
import com.wine.easy.canal.web.adapter.RdbAdapter;
import com.wine.easy.canal.web.entity.EtlResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.web
 * @ClassName CanalClientELKController
 * @Author qiang.li
 * @Date 2021/6/1 4:28 下午
 * @Description TODO
 */
public class CanalClientELKController {
    private static final Logger log= LoggerFactory.getLogger(CanalClientELKController.class);
    public RdbAdapter rdbAdapter=null;
    private ELKConfig elkConfig;
    public CanalClientELKController(Register register, ELKConfig elkConfig) {
        this.rdbAdapter=new RdbAdapter( register,elkConfig);
        try {
            rdbAdapter.init();
        } catch (IOException e) {
            log.error("初始化adapter发生异常",e);
        }
    }

    public EtlResult elk(String task, String condition, List<String> params){
        return rdbAdapter.etl(task,condition,params);
    }

}
