package com.easy.canal.test;

import com.wine.easy.canal.config.CanalClientConfig;
import com.wine.easy.canal.yaml.CanalClientConfigurationLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Project easy-canal-parent
 * @PackageName com.easy.canal.test
 * @ClassName Start
 * @Author qiang.li
 * @Date 2021/3/24 11:33 上午
 * @Description TODO
 */
public class Start {
    public static void main(String[] args) throws IOException {
        CanalClientConfig canalClientConfig= CanalClientConfigurationLoader.load();
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/spring/application-context.xml");
    }
}
