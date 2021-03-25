package com.wine.easy.canal.core;

import java.util.List;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.core
 * @ClassName EditInfo
 * @Author qiang.li
 * @Date 2021/3/24 9:47 下午
 * @Description TODO
 */
public class EditInfo {
    private Object after;
    private Object before;
    private List<String> updatedProperty;

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

    public List<String> getUpdatedProperty() {
        return updatedProperty;
    }

    public void setUpdatedProperty(List<String> updatedProperty) {
        this.updatedProperty = updatedProperty;
    }
}
