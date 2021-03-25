package com.wine.easy.canal.type;

import java.math.BigDecimal;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.type
 * @ClassName IntegerTypeHandler
 * @Author qiang.li
 * @Date 2021/3/24 5:35 下午
 * @Description TODO
 */
public class IntegerTypeHandler extends BaseTypeHandler<Integer> {
    @Override
    public Integer convert(Object value) {
        return new Integer(value.toString());
    }
}
