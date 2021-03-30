package com.wine.easy.canal.exception;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.exception
 * @ClassName TypeException
 * @Author qiang.li
 * @Date 2021/3/29 2:01 下午
 * @Description TODO
 */
public class TypeException extends PersistenceException {
    private static final long serialVersionUID = 8614420898975117130L;

    public TypeException() {
    }

    public TypeException(String message) {
        super(message);
    }

    public TypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeException(Throwable cause) {
        super(cause);
    }
}
