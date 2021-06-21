package com.wine.easy.canal.core;

import com.wine.easy.canal.config.CanalClientConfig;
import com.wine.easy.canal.config.GroupConfig;
import com.wine.easy.canal.interfaces.ProcessListener;
import com.wine.easy.canal.interfaces.Register;
import com.wine.easy.canal.web.HttpServerLoader;
import com.wine.easy.canal.yaml.CanalClientConfigurationLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
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
public class CanalLoader implements InitializingBean, ApplicationContextAware, DisposableBean {
    private CanalClientConfig canalInfoConfig;
    private Register register;
    private ApplicationContext applicationContext;
    private Map<String,CanalListenerWorker> workerMap=new HashMap<>();
    private HttpServerLoader httpServerLoader;


    @Override
    public void afterPropertiesSet() throws Exception {
        canalInfoConfig= CanalClientConfigurationLoader.load();
        register=new ProcessListenerRegister(applicationContext,canalInfoConfig);
        //注册监听器
        register.register(applicationContext.getBeansOfType(ProcessListener.class));
        for (String group:
             register.getGroups()) {
            GroupConfig groupConfig= canalInfoConfig.getGroups().get(group);
            if(groupConfig==null){
                continue;
            }
            groupConfig.setName(group);
            //启动working
            CanalListenerWorker  canalListenerWorker=new CanalListenerWorker(this,groupConfig);
            canalListenerWorker.start();
            workerMap.put(group,canalListenerWorker);
        }
        httpServerLoader =new HttpServerLoader(register);
        httpServerLoader.start();


    }

    @Override
    public void destroy() throws Exception {
        for (CanalListenerWorker canalListenerWorker:
        workerMap.values()) {
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

    public CanalClientConfig getCanalInfoConfig() {
        return canalInfoConfig;
    }
}
