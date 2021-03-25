package com.wine.easy.canal.interfaces;

import java.util.List;
import java.util.Map;

/**
 * @Project easy-canal
 * @PackageName com.wine.easy.canal.annotation
 * @ClassName Listener
 * @Author qiang.li
 * @Date 2021/3/23 6:01 下午
 * @Description TODO
 */
public interface ProcessListener<T> {
    public boolean update(T after, T before, List<String> updateFiled);
    public boolean insert(T t);
    public boolean delete(T t);
}
