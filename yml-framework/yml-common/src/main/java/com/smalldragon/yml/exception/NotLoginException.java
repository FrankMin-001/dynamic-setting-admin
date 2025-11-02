package com.smalldragon.yml.exception;

/**
 * 用户未登录异常
 * @author YML
 */
public class NotLoginException extends RuntimeException {
    private final int code;

    public NotLoginException(int code, String message) {
        super(message);
        this.code = code;
    }

    public NotLoginException(String message) {
        super(message);
        this.code = 401;
    }

    public NotLoginException() {
        super("未登录或登录已过期");
        this.code = 401;
    }

    public int getCode() {
        return code;
    }
}
