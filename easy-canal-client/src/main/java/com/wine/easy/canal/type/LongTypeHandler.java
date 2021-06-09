package com.wine.easy.canal.type;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.type
 * @ClassName LongTypeHandler
 * @Author qiang.li
 * @Date 2021/3/24 5:49 下午
 * @Description TODO
 */
public class LongTypeHandler extends BaseTypeHandler<Long> {
    @Override
    public Long convert(Object value) {
        return new Long(value.toString());
    }
}
