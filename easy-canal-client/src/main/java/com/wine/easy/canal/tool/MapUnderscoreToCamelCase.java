package com.wine.easy.canal.tool;

import com.google.common.base.CaseFormat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.tool
 * @ClassName Test
 * @Author qiang.li
 * @Date 2021/3/24 4:21 下午
 * @Description TODO
 */
public class MapUnderscoreToCamelCase {
    private static ConcurrentHashMap<String,String> names=new ConcurrentHashMap<>();
    public static String convertByCache(String name){
        if(names.containsKey(name)) {
          return names.get(name);
        }
        String convertName= CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
        names.put(name,convertName);
        return convertName;
    }
}
