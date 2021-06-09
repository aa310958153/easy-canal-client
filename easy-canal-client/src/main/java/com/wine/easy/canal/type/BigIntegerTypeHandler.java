package com.wine.easy.canal.type;

import java.math.BigInteger;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.type
 * @ClassName BigIntegerTypeHandler
 * @Author qiang.li
 * @Date 2021/3/24 5:50 下午
 * @Description TODO
 */
public class BigIntegerTypeHandler extends BaseTypeHandler<BigInteger> {
    @Override
    public BigInteger convert(Object value) {
        return new BigInteger(value.toString());
    }
}
