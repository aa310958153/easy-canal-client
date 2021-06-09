package com.wine.easy.canal.type;

import java.text.ParseException;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.type
 * @ClassName StringTypeHandler
 * @Author qiang.li
 * @Date 2021/3/24 7:32 下午
 * @Description TODO
 */
public class StringTypeHandler extends BaseTypeHandler<String> {
    @Override
    public String convert(Object value) throws ParseException {
        return value.toString();
    }
}
