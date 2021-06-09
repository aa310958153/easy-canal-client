package com.wine.easy.canal.exception;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.exception
 * @ClassName ICanalException
 * @Author qiang.li
 * @Date 2021/3/29 2:02 下午
 * @Description TODO
 */
public class ICanalException extends RuntimeException {
    private static final long serialVersionUID = 3880206998166270511L;

    public ICanalException() {
    }

    public ICanalException(String message) {
        super(message);
    }

    public ICanalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ICanalException(Throwable cause) {
        super(cause);
    }
}
