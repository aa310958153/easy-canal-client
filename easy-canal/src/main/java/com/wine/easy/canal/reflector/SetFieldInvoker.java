package com.wine.easy.canal.reflector;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.reflector
 * @ClassName f
 * @Author qiang.li
 * @Date 2021/3/24 3:31 下午
 * @Description TODO
 */

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 封装filed元数据信息
 */
public class SetFieldInvoker implements Invoker {
    private Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    //给指定对象的当前属性设置值
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        this.field.set(target, args[0]);
        return null;
    }

    public Class<?> getType() {
        return this.field.getType();
    }
}
