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
 * @Description TODO
 */
@Component
public class CanalContext implements InitializingBean, ApplicationContextAware, EnvironmentAware, DisposableBean {
    @Autowired
    private CanalInfoConfig canalInfoConfig;
    private Register register;
    private ApplicationContext applicationContext;
    private Environment environment;
    private   CanalListenerWorker canalListenerWorker;

    private String host;

    @Override
    public void afterPropertiesSet() throws Exception {
        register=new ProcessListenerRegister();
        register.register(applicationContext.getBeansOfType(ProcessListener.class));
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
    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }

    public CanalInfoConfig getCanalInfoConfig() {
        return canalInfoConfig;
    }

    public void setCanalInfoConfig(CanalInfoConfig canalInfoConfig) {
        this.canalInfoConfig = canalInfoConfig;
    }
}
