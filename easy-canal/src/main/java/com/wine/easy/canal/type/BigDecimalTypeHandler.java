package com.wine.easy.canal.type;

import java.math.BigDecimal;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.type
 * @ClassName In
 * @Author qiang.li
 * @Date 2021/3/24 5:27 下午
 * @Description TODO
 */
public class BigDecimalTypeHandler extends BaseTypeHandler<BigDecimal> {
    @Override
    public BigDecimal convert(Object value) {
        return  new BigDecimal(value.toString());
    }
}
