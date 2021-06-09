package com.wine.easy.canal.type;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.type
 * @ClassName TimestampTypeHandler
 * @Author qiang.li
 * @Date 2021/3/24 6:01 下午
 * @Description TODO
 */
public class TimestampTypeHandler   extends BaseTypeHandler<Timestamp> {
    @Override
    public Timestamp convert(Object value) throws ParseException {
        String  valueStr=value.toString();
        if(valueStr.indexOf("-")>0){
            SimpleDateFormat sdf  =   new  SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            return new Timestamp(sdf.parse(valueStr).getTime());
        }
        return new Timestamp(Long.valueOf(value.toString()));
    }
}
