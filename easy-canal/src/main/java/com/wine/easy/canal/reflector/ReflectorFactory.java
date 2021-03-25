package com.wine.easy.canal.reflector;

import com.wine.easy.canal.exception.ReflectionException;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.reflector
 * @ClassName d
 * @Author qiang.li
 * @Date 2021/3/24 3:28 下午
 * @Description TODO
 */
public interface ReflectorFactory {
    boolean isClassCacheEnabled();

    void setClassCacheEnabled(boolean var1);

    Reflector findForClass(Class<?> var1) throws ReflectionException;
}