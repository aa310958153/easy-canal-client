package com.wine.easy.canal.reflector;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.reflector
 * @ClassName f
 * @Author qiang.li
 * @Date 2021/3/24 3:32 下午
 * @Description TODO
 */

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 封装Filed信息 提供getfiled的调用实现
 */
public class GetFieldInvoker implements Invoker {
    private Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
    }

    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        return this.field.get(target);
    }

    public Class<?> getType() {
        return this.field.getType();
    }
}