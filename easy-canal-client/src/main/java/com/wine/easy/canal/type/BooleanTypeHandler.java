package com.wine.easy.canal.type;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.type
 * @ClassName BooleanTypeHandler
 * @Author qiang.li
 * @Date 2021/3/24 5:51 下午
 * @Description TODO
 */
public class BooleanTypeHandler extends BaseTypeHandler<Boolean>{
    @Override
    public Boolean convert(Object value) {
        return new Boolean(value.toString());
    }
}
