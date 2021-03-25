package com.wine.easy.canal.type;

import java.text.ParseException;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.type
 * @ClassName BaseTypeHandler
 * @Author qiang.li
 * @Date 2021/3/24 5:30 下午
 * @Description TODO
 */
public abstract class BaseTypeHandler<T> implements TypeHandler<T> {
    public T convertNullableResult(Object value) throws ParseException {
        if(value==null){
            return null;
        }
        return convert(value);
    }
    public abstract T convert(Object value) throws ParseException;

}
