package com.wine.easy.canal.core;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.wine.easy.canal.exception.ReflectionException;
import com.wine.easy.canal.interfaces.ProcessListener;
import com.wine.easy.canal.reflector.*;
import com.wine.easy.canal.tool.Dml;
import com.wine.easy.canal.tool.MapUnderscoreToCamelCase;
import com.wine.easy.canal.type.TypeHandler;
import com.wine.easy.canal.type.TypeHandlerRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project easy-canal
 * @PackageName com.wine.easy.canal.core
 * @ClassName ListenerMethodArgumentResolver
 * @Author qiang.li
 * @Date 2021/3/24 11:04 上午
 * @Description TODO
 */
public class ListenerMethodArgumentResolver {
    private static final Logger logger = LoggerFactory.getLogger(ListenerMethodArgumentResolver.class);
    public Map<ProcessListener, Class> classNameMappingClass = new HashMap<>();
    private ObjectFactory objectFactory = new DefaultObjectFactory();
    private ReflectorFactory reflectorFactory = new DefaultReflectorFactory();

    public Class getArgumentClass(ProcessListener<?> processListener) {
        if (!classNameMappingClass.containsKey(processListener)) {
            Type[] types = processListener.getClass().getGenericInterfaces();
            ParameterizedType parameterized = (ParameterizedType) types[0];
            Class clazz = (Class) parameterized.getActualTypeArguments()[0];
            classNameMappingClass.put(processListener, clazz);
        }
        return classNameMappingClass.get(processListener);
    }
      public List<EditMetaInfo> resolver(ProcessListener<?> processListener, Dml dml) throws InvalidProtocolBufferException, ReflectionException, InvocationTargetException, IllegalAccessException, ParseException {
         Class c = getArgumentClass(processListener);
         List<EditMetaInfo> editMetaInfos=new ArrayList<EditMetaInfo>();
          for (Dml.Row row:
          dml.getData()) {
              EditMetaInfo editInfo=new EditMetaInfo();
              editInfo.setAfter(columnsConvertObject(c,row.getData()));
              editInfo.setBefore(columnsConvertObject(c,row.getOld()));
              editMetaInfos.add(editInfo);
          }
          return editMetaInfos;
    }

    public List<EditMetaInfo> resolver(ProcessListener<?> processListener, List<Map<String,Object>> rows) throws InvalidProtocolBufferException, ReflectionException, InvocationTargetException, IllegalAccessException, ParseException {
        Class c = getArgumentClass(processListener);
        List<EditMetaInfo> editMetaInfos=new ArrayList<EditMetaInfo>();
        for (Map<String,Object> row:
                rows) {
            EditMetaInfo editInfo=new EditMetaInfo();
            editInfo.setAfter(columnsConvertObject(c,row));
            editMetaInfos.add(editInfo);
        }

        return editMetaInfos;
    }
    public List<EditMetaInfo> resolver(ProcessListener<?> processListener, CanalEntry.Entry entry) throws InvalidProtocolBufferException, ReflectionException, InvocationTargetException, IllegalAccessException, ParseException {
        Class c = getArgumentClass(processListener);
        CanalEntry.RowChange change;
        try {
            change = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        } catch (InvalidProtocolBufferException e) {
            logger.error("canalEntry_parser_error,根据CanalEntry获取RowChange失败！", e);
            throw e;
        }
        List<CanalEntry.RowData> rowDatas = change.getRowDatasList();
        if (CollectionUtils.isEmpty(rowDatas)) {
            return null;
        }
        List<EditMetaInfo> objects=new ArrayList<>();
        for (CanalEntry.RowData rowData :
                rowDatas) {
            EditMetaInfo editInfo=new EditMetaInfo();
            editInfo.setAfter(columnsConvertObject(c,rowData.getAfterColumnsList()));
            editInfo.setBefore(columnsConvertObject(c,rowData.getBeforeColumnsList()));
//            editInfo.setUpdatedProperty(getUpdatedUpdatedProperty(rowData.getAfterColumnsList()));
            objects.add(editInfo);
        }
        return objects;
    }

    public List<String> getUpdatedUpdatedProperty(List<CanalEntry.Column> columns){
        if(CollectionUtils.isEmpty(classNameMappingClass)){
            return null;
        }
        List<String> updatedProperty=new ArrayList<>();
        for (CanalEntry.Column column :
                columns) {
            if(!column.getUpdated()){
                continue;
            }
            updatedProperty.add(column.getName());
        }
        return updatedProperty;

    }
    public Object columnsConvertObject(Class c, List<CanalEntry.Column> columns) throws ReflectionException, InvocationTargetException, IllegalAccessException, ParseException {
        Object o = objectFactory.create(c);
        Reflector classesReflector= reflectorFactory.findForClass(c);
        for (CanalEntry.Column column :
                columns) {
            String filedName=MapUnderscoreToCamelCase.convertByCache(column.getName());
            if (!classesReflector.hasGetter(filedName)) {
                continue;
            }
            Type setterType= classesReflector.getSetterType(filedName);
            TypeHandler typeHandler= TypeHandlerRegister.getTypeHandler(setterType);
            if(typeHandler==null){
                logger.error("未适配到typeHandle{},name:{},value:{},",setterType,filedName,column.getValue());
            }
            classesReflector.getSetInvoker(filedName).invoke(o,new Object[]{typeHandler.convert(column.getValue())});
        }
        return o;
    }
    public Object columnsConvertObject(Class c,Map<String,Object> columns) throws ReflectionException, InvocationTargetException, IllegalAccessException, ParseException {
        if(columns==null||columns.isEmpty()){
            return null;
        }
        Object o = objectFactory.create(c);
        Reflector classesReflector= reflectorFactory.findForClass(c);
        for (String columnName:
                columns.keySet()) {
            String filedName=MapUnderscoreToCamelCase.convertByCache(columnName);
            Object value=columns.get(columnName);
            if(value==null){
                continue;
            }
            if (!classesReflector.hasGetter(filedName)) {
                continue;
            }
            Type setterType= classesReflector.getSetterType(filedName);
            TypeHandler typeHandler= TypeHandlerRegister.getTypeHandler(setterType);
            if(typeHandler==null){
                logger.error("未适配到typeHandle{},name:{},value:{},",setterType,filedName,value);
            }
            classesReflector.getSetInvoker(filedName).invoke(o,new Object[]{typeHandler.convert(value)});
        }
        return o;
    }



}
