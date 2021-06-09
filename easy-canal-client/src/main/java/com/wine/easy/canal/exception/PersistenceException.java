package com.wine.easy.canal.exception;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.exception
 * @ClassName PersistenceException
 * @Author qiang.li
 * @Date 2021/3/29 2:02 下午
 * @Description TODO
 */
public class PersistenceException extends ICanalException {
    private static final long serialVersionUID = -7537395265357977271L;

    public PersistenceException() {
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
