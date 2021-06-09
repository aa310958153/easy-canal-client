package com.wine.easy.canal.type;

import java.text.ParseException;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.type
 * @ClassName EnumTypeHandler
 * @Author qiang.li
 * @Date 2021/3/25 4:57 下午
 * @Description TODO
 */
public class EnumTypeHandler<E extends Enum<E>>  extends BaseTypeHandler<E>{
    private final Class<E> type;
    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        } else {
            this.type = type;
        }
    }
    @Override
    public E convert(Object value) throws ParseException {
        return Enum.valueOf(type,value.toString());
    }
}
