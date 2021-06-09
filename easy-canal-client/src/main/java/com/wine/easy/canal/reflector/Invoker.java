package com.wine.easy.canal.reflector;

import java.lang.reflect.InvocationTargetException;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.reflector
 * @ClassName Invoker
 * @Author qiang.li
 * @Date 2021/3/24 3:30 下午
 * @Description TODO
 */
public interface Invoker {
    Object invoke(Object var1, Object[] var2) throws IllegalAccessException, InvocationTargetException;

    Class<?> getType();
}
