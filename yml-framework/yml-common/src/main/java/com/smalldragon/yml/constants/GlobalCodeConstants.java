package com.smalldragon.yml.constants;

/**
 * @Author YML
 * @Date 2025/3/2 16:40
 **/
public class GlobalCodeConstants {
    public final static Integer SUCCESS_CODE = 2000; // 响应成功
    public final static Integer ERROR_CODE = 5000; // 服务端错误
    public final static Integer AUTH_CODE = 4000; // 客户端错误

    public final static Integer REQUESTBODY_VALID = 4001; // 请求体参数校验异常
    public final static Integer REQUESTPARAM_VALID = 4002; // 处理方法参数校验异常
    public final static Integer UN_LOGIN = 4003; // 用户未登录异常

}
