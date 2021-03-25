package com.easy.canal.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Project easy-canal-parent
 * @PackageName com.easy.canal.test
 * @ClassName Start
 * @Author qiang.li
 * @Date 2021/3/24 11:33 上午
 * @Description TODO
 */
public class Start {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/spring/application-context.xml");
    }
}
