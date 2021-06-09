package com.wine.easy.canal.reflector;

import com.wine.easy.canal.exception.ReflectionException;

import java.util.List;
import java.util.Properties;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.reflector
 * @ClassName ObjectFactory
 * @Author qiang.li
 * @Date 2021/3/24 3:47 下午
 * @Description TODO
 */
public interface ObjectFactory {
    void setProperties(Properties var1);

    <T> T create(Class<T> var1) throws ReflectionException;

    <T> T create(Class<T> var1, List<Class<?>> var2, List<Object> var3) throws ReflectionException;

    <T> boolean isCollection(Class<T> var1);
}