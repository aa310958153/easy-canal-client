package com.wine.easy.canal.type;

import java.text.ParseException;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.type
 * @ClassName TypeHandler
 * @Author qiang.li
 * @Date 2021/3/24 5:22 下午
 * @Description TODO
 */
public interface TypeHandler<T> {
    public T convert(Object value) throws ParseException;
}
