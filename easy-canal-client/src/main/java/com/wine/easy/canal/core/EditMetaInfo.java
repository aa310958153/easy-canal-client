package com.wine.easy.canal.core;

import java.util.List;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.core
 * @ClassName EditInfo
 * @Author qiang.li
 * @Date 2021/3/24 9:47 下午
 * @Description 用于封装修改数据的Vo
 */
public class EditMetaInfo {
    private Object after;
    private Object before;


    public Object getAfter() {
        return after;
    }

    public void setAfter(Object after) {
        this.after = after;
    }

    public Object getBefore() {
        return before;
    }

    public void setBefore(Object before) {
        this.before = before;
    }

}
