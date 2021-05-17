package com.wine.easy.canal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示监听binlog表改动的处理器
 */
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * 数据表名称注解，默认值为类名称
     * @return
     */
    public String name();



}
