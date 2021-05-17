package com.wine.easy.canal.core;

import com.wine.easy.canal.interfaces.ProcessListener;
import com.wine.easy.canal.interfaces.Register;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Project easy-canal
 * @PackageName com.wine.easy.canal.core
 * @ClassName CanalContext
 * @Author qiang.li
 * @Date 2021/3/23 6:19 下午
 * @Description 初始化入口
 */
@Component
public class CanalContext implements InitializingBean, ApplicationContextAware, DisposableBean {
    @Autowired
    private CanalInfoConfig canalInfoConfig;
    private Register register;
    private ApplicationContext applicationContext;
    private CanalListenerWorker canalListenerWorker;


    @Override
    public void afterPropertiesSet() throws Exception {
        register=new ProcessListenerRegister();
        //注册监听器
        register.register(applicationContext.getBeansOfType(ProcessListener.class));
        //启动working
        canalListenerWorker=new CanalListenerWorker(this);
        canalListenerWorker.start();
    }

    @Override
    public void destroy() throws Exception {
        if(canalListenerWorker!=null){
            canalListenerWorker.end();
        }
    }

    public Register getRegister() {
        return register;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
         this.applicationContext=applicationContext;
    }

    public CanalInfoConfig getCanalInfoConfig() {
        return canalInfoConfig;
    }
}
