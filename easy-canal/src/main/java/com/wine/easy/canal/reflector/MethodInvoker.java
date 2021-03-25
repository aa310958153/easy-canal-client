package com.wine.easy.canal.reflector;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.reflector
 * @ClassName d
 * @Author qiang.li
 * @Date 2021/3/24 3:32 下午
 * @Description TODO
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 方法元数据封装 以及提供调用的方法
 */
public class MethodInvoker implements Invoker {
    private Class<?> type;
    private Method method;

    public MethodInvoker(Method method) {
        this.method = method;
        if (method.getParameterTypes().length == 1) {
            this.type = method.getParameterTypes()[0];
        } else {
            this.type = method.getReturnType();
        }

    }

    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        return this.method.invoke(target, args);
    }

    public Class<?> getType() {
        return this.type;
    }
}