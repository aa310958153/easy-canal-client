package com.wine.easy.canal.type;


import com.wine.easy.canal.exception.TypeException;

import java.lang.reflect.Constructor;
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
    private static Map<Type,TypeHandler>  typeHandlerMap=new HashMap<>();
    private static Class< ? extends TypeHandler> defaultEnumTypeHandler=EnumTypeHandler.class;
    static {
        register((Type)Integer.class,new IntegerTypeHandler());
        register((Class)Integer.TYPE, new IntegerTypeHandler());
        register((Type)BigDecimal.class,new BigDecimalTypeHandler());
        register((Type)BigInteger.class,new BigIntegerTypeHandler());
        register((Type)Boolean.class,new BooleanTypeHandler());
        register((Type)Boolean.TYPE,new BigDecimalTypeHandler());
        register((Type)Date.class,new DateTypeHandler());
        register((Type)Long.class,new LongTypeHandler());
        register((Type)Long.TYPE,new LongTypeHandler());
        register((Type)Timestamp.class,new TimestampTypeHandler());
        register((Type)String.class,new StringTypeHandler());
    }
    public static TypeHandler getTypeHandler(Type c){
        if(!typeHandlerMap.containsKey(c)){
            if (Enum.class.isAssignableFrom((Class)c)){
                Class cs=(Class)c;
                Class<?> enumClass = cs.isAnonymousClass() ? cs.getSuperclass() : cs;
                register(c,(TypeHandler)getInstance(enumClass,defaultEnumTypeHandler));
            }
        }
        TypeHandler typeHandler= typeHandlerMap.get(c);
        return typeHandler;

    }
    public static void register(Type c,TypeHandler t){
        typeHandlerMap.put(c,t);
    }

    public static <T> TypeHandler<T> getInstance(Class<?> javaTypeClass, Class<?> typeHandlerClass) {
        Constructor c;
        if (javaTypeClass != null) {
            try {
                c = typeHandlerClass.getConstructor(Class.class);
                return (TypeHandler)c.newInstance(javaTypeClass);
            } catch (NoSuchMethodException var5) {
            } catch (Exception var6) {
                throw new TypeException("Failed invoking constructor for handler " + typeHandlerClass, var6);
            }
        }

        try {
            c = typeHandlerClass.getConstructor();
            return (TypeHandler)c.newInstance();
        } catch (Exception var4) {
            throw new TypeException("Unable to find a usable constructor for " + typeHandlerClass, var4);
        }
    }
}
