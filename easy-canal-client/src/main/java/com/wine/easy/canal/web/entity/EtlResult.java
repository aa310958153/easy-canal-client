package com.wine.easy.canal.web.entity;

import java.io.Serializable;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.web.entity
 * @ClassName EtlResult
 * @Author qiang.li
 * @Date 2021/6/1 5:29 下午
 * @Description TODO
 */
public class EtlResult implements Serializable {
    private static final long serialVersionUID = 4250522736289866505L;

    private boolean           succeeded        = false;

    private String            resultMessage;

    private String            errorMessage;

    public boolean getSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
