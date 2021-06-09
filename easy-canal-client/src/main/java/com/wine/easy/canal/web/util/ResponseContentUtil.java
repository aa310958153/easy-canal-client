package com.wine.easy.canal.web.util;

import com.wine.easy.canal.web.entity.ResponseContent;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.web
 * @ClassName ResponseContentUtil
 * @Author qiang.li
 * @Date 2021/6/1 4:23 下午
 * @Description TODO
 */
public final class ResponseContentUtil {

    /**
     * Build the successful response without data model.
     *
     * @return response result
     */
    public static ResponseContent<?> success() {
        return build(null);
    }

    /**
     * Build the successful response with data model.
     *
     * @param model data model
     * @param <T> data model type
     * @return response result
     */
    public static <T> ResponseContent<T> build(final T model) {
        ResponseContent<T> result = new ResponseContent<>();
        result.setSuccess(true);
        result.setModel(model);
        return result;
    }

    /**
     * Build the bad request response.
     *
     * @param errorMsg error message
     * @return response result
     */
    public static ResponseContent<?> handleBadRequest(final String errorMsg) {
        ResponseContent<?> result = new ResponseContent<>();
        result.setSuccess(false);
        result.setErrorCode(ResponseCode.BAD_REQUEST);
        result.setErrorMsg(errorMsg);
        return result;
    }

    /**
     * Build the error response of exception.
     *
     * @param errorMsg error message
     * @return response result
     */
    public static ResponseContent<?> handleException(final String errorMsg) {
        ResponseContent<?> result = new ResponseContent<>();
        result.setSuccess(false);
        result.setErrorCode(ResponseCode.SERVER_ERROR);
        result.setErrorMsg(errorMsg);
        return result;
    }
}
