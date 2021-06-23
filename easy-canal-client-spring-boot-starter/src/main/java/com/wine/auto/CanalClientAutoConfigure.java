package com.wine.auto;

import com.wine.easy.canal.config.CanalClientConfig;
import com.wine.easy.canal.core.CanalLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.auto
 * @ClassName CanalClientAutoConfigure
 * @Author qiang.li
 * @Date 2021/6/22 4:20 下午
 * @Description TODO
 */
@Configuration
@ConditionalOnClass(CanalClientConfig.class)//当Spring Context中不存在该Bean时。
public class CanalClientAutoConfigure {
    @Bean
    CanalLoader initCanalLoader (){
        return  new CanalLoader();
    }
}
