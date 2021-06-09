package com.wine.easy.canal.exception;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.exception
 * @ClassName ReflectionException
 * @Author qiang.li
 * @Date 2021/3/24 3:35 下午
 * @Description TODO
 */

public class ReflectionException extends Exception {
    private static final long serialVersionUID = 7642570221267566591L;

    public ReflectionException() {
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }
}
