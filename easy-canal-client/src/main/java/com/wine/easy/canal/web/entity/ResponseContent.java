package com.wine.easy.canal.web.entity;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.web.entity
 * @ClassName ResponseContent
 * @Author qiang.li
 * @Date 2021/6/1 4:24 下午
 * @Description TODO
 */

public final class ResponseContent<T> {

    private boolean success = true;

    private int errorCode;

    private String errorMsg;

    private T model;

    public boolean isSuccess() {
        return success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}