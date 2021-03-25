package com.wine.easy.canal.type;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.type
 * @ClassName TypeHandlerRegister
 * @Author qiang.li
 * @Date 2021/3/24 5:38 下午
 * @Description TODO
 */
public class TypeHandlerRegister {
    private static Map<Class,TypeHandler>  typeHandlerMap=new HashMap<>();
    static {
        typeHandlerMap.put(Integer.class,new IntegerTypeHandler());
        typeHandlerMap.put(BigDecimal.class,new BigDecimalTypeHandler());
        typeHandlerMap.put(BigInteger.class,new BigIntegerTypeHandler());
        typeHandlerMap.put(Boolean.class,new BooleanTypeHandler());
        typeHandlerMap.put(Date.class,new DateTypeHandler());
        typeHandlerMap.put(Integer.class,new IntegerTypeHandler());
        typeHandlerMap.put(Long.class,new LongTypeHandler());
        typeHandlerMap.put(Timestamp.class,new TimestampTypeHandler());
        typeHandlerMap.put(String.class,new StringTypeHandler());
    }
    public static TypeHandler getTypeHandler(Class c){
        return typeHandlerMap.get(c);
    }
}
