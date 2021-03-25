package com.wine.easy.canal.reflector;

import com.wine.easy.canal.exception.ReflectionException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.reflector
 * @ClassName d
 * @Author qiang.li
 * @Date 2021/3/24 3:29 下午
 * @Description TODO
 */
public class DefaultReflectorFactory implements ReflectorFactory {
    private boolean classCacheEnabled = true;
    //将反射的元数据信息封装保存到Reflector 大大提交了性能
    private final ConcurrentMap<Class<?>, Reflector> reflectorMap = new ConcurrentHashMap();

    public DefaultReflectorFactory() {
    }

    public boolean isClassCacheEnabled() {
        return this.classCacheEnabled;
    }

    public void setClassCacheEnabled(boolean classCacheEnabled) {
        this.classCacheEnabled = classCacheEnabled;
    }

    /**
     * 获得指定类型反射元数据信息
     * @param type
     * @return
     */
    public Reflector findForClass(Class<?> type) throws ReflectionException {
        if (this.classCacheEnabled) {
            Reflector cached = (Reflector)this.reflectorMap.get(type);
            //如果没有缓存则从缓存里面拿
            if (cached == null) {
                cached = new Reflector(type);
                this.reflectorMap.put(type, cached);
            }

            return cached;
        } else {
            return new Reflector(type);
        }
    }
}