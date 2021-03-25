package com.wine.easy.canal.reflector;

import com.wine.easy.canal.exception.ReflectionException;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.reflector
 * @ClassName d
 * @Author qiang.li
 * @Date 2021/3/24 3:48 下午
 * @Description TODO
 */
public class DefaultObjectFactory implements ObjectFactory, Serializable {
    private static final long serialVersionUID = -8855120656740914948L;

    public DefaultObjectFactory() {
    }

    /**
     * 创建指定类型的对象 不使用构造函数创建
     * @param type 类型
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> type) throws ReflectionException {
        return (T) this.create(type, (List)null, (List)null);
    }

    /**
     * 创建指定类型的对象
     * @param type 类型
     * @param constructorArgTypes 构造函数参数类型列表
     * @param constructorArgs 构造函数参数列表
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) throws ReflectionException {
        Class<?> classToCreate = this.resolveInterface(type);
        return (T) this.instantiateClass(classToCreate, constructorArgTypes, constructorArgs);
    }

    public void setProperties(Properties properties) {
    }

    <T> T instantiateClass(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) throws ReflectionException {
        try {
            Constructor constructor;
            //判断是否指定了构造函数初始化
            if (constructorArgTypes != null && constructorArgs != null) {
                //获得private或public指定参数类型列表的构造函数 注:getConstructor和getDeclaredConstructor的区别是只能获得public
                constructor = type.getDeclaredConstructor((Class[])constructorArgTypes.toArray(new Class[constructorArgTypes.size()]));
                //如果是私有的 设置可以访问
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }

                return (T) constructor.newInstance(constructorArgs.toArray(new Object[constructorArgs.size()]));
            } else {
                constructor = type.getDeclaredConstructor();
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }

                return (T) constructor.newInstance();
            }
        } catch (Exception var9) {
            StringBuilder argTypes = new StringBuilder();
            if (constructorArgTypes != null && !constructorArgTypes.isEmpty()) {
                Iterator i$ = constructorArgTypes.iterator();

                while(i$.hasNext()) {
                    Class<?> argType = (Class)i$.next();
                    argTypes.append(argType.getSimpleName());
                    argTypes.append(",");
                }

                argTypes.deleteCharAt(argTypes.length() - 1);
            }

            StringBuilder argValues = new StringBuilder();
            if (constructorArgs != null && !constructorArgs.isEmpty()) {
                Iterator i$ = constructorArgs.iterator();

                while(i$.hasNext()) {
                    Object argValue = i$.next();
                    argValues.append(String.valueOf(argValue));
                    argValues.append(",");
                }

                argValues.deleteCharAt(argValues.length() - 1);
            }

            throw new ReflectionException("Error instantiating " + type + " with invalid types (" + argTypes + ") or values (" + argValues + "). Cause: " + var9, var9);
        }
    }

    /**
     * 如果是定义结合类型 类型改为实现类
     * @param type
     * @return
     */
    protected Class<?> resolveInterface(Class<?> type) {
        Class classToCreate;
        if (type != List.class && type != Collection.class && type != Iterable.class) {
            if (type == Map.class) {
                classToCreate = HashMap.class;
            } else if (type == SortedSet.class) {
                classToCreate = TreeSet.class;
            } else if (type == Set.class) {
                classToCreate = HashSet.class;
            } else {
                classToCreate = type;
            }
        } else {
            classToCreate = ArrayList.class;
        }

        return classToCreate;
    }

    public <T> boolean isCollection(Class<T> type) {
        return Collection.class.isAssignableFrom(type);
    }
}