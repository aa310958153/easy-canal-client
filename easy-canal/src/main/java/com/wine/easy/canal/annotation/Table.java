package com.wine.easy.canal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * 数据表名称注解，默认值为类名称
     * @return
     */
    public String name();



}
