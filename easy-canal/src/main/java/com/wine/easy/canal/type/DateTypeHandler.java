package com.wine.easy.canal.type;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.type
 * @ClassName DateTypeHandler
 * @Author qiang.li
 * @Date 2021/3/24 5:51 下午
 * @Description TODO
 */
public class DateTypeHandler extends BaseTypeHandler<Date> {

    @Override
    public Date convert(Object value) throws ParseException {
        String  valueStr=value.toString();
        if(valueStr.indexOf("-")>0){
            SimpleDateFormat sdf  =   new  SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            return sdf.parse(valueStr);
        }
        return new Timestamp(Long.valueOf(value.toString()));
    }
}
