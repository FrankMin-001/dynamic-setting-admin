package com.smalldragon.yml.pojo;

import com.smalldragon.yml.constants.GlobalCodeConstants;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommonResult<T> implements Serializable {

    private int code; // 状态码
    private String msg; // 消息
    private T data; // 数据

    // 私有构造方法，防止外部直接创建对象
    private CommonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功响应，默认消息为"执行成功"
    public static <T> CommonResult<T> ok(T data) {
        return new CommonResult<>(GlobalCodeConstants.SUCCESS_CODE, "执行成功", data);
    }

    // 成功响应，自定义消息
    public static <T> CommonResult<T> ok(String msg, T data) {
        return new CommonResult<>(2000, msg, data);
    }

    // 失败响应，携带异常编码和异常消息
    public static <T> CommonResult<T> error(int code, String msg) {
        return new CommonResult<>(code, msg, null);
    }
    // 失败响应
    public static <T> CommonResult<T> error(int code, String msg,T data) {
        return new CommonResult<>(code, msg, data);
    }
}
