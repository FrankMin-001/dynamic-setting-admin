package com.smalldragon.yml.Exception;

import com.smalldragon.yml.constants.GlobalCodeConstants;
import com.smalldragon.yml.exception.BusinessException;
import com.smalldragon.yml.exception.NotLoginException;
import com.smalldragon.yml.pojo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author YML
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有 RuntimeException 异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<Void> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage());
        // 返回自定义的错误响应
        return CommonResult.error(GlobalCodeConstants.ERROR_CODE, e.getMessage());
    }

    /**
     * 处理所有 Exception 异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 返回 HTTP 500 状态码
    public CommonResult<Void> handleException(Exception e) {
        log.error(e.getMessage());
        // 返回自定义的错误响应
        return CommonResult.error(GlobalCodeConstants.ERROR_CODE, "系统内部错误，请稍后重试");
    }


    /**
     * 处理参数绑定异常（@RequestParam 或 @PathVariable 触发的异常）
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<Map<String, String>> handleBindException(BindException e) {
        log.error(e.getMessage());
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return CommonResult.error(GlobalCodeConstants.AUTH_CODE, "参数绑定失败", errors);
    }

    /**
     * 处理请求体参数校验异常（@RequestBody + @Valid 触发的异常）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 返回 HTTP 400 状态码
    public CommonResult<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return CommonResult.error(GlobalCodeConstants.REQUESTBODY_VALID, "参数校验失败", errors);
    }

    /**
     * 处理方法参数校验异常（@RequestParam + @Validated 触发的异常）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 返回 HTTP 400 状态码
    public CommonResult<Map<String, String>> handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage());

        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        violations.forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(field, message);
        });
        return CommonResult.error(GlobalCodeConstants.REQUESTPARAM_VALID, "参数校验失败", errors);
    }

    /**
     * 处理自定义业务异常: 自己定义异常的编码,可提前规定后面出错搜索日志,看编号就能找对对应出错模块
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 返回 HTTP4000
    public CommonResult<Void> handleBusinessException(BusinessException e) {
        log.error(e.getMessage());

        return CommonResult.error(GlobalCodeConstants.AUTH_CODE, e.getMessage());
    }

    /**
     * 处理用户未登录异常: 对Sa-Token的登录认证异常进行处理
     */
    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 返回 HTTP 4003 状态码
    public CommonResult<Void> handleBusinessException(NotLoginException e) {
        log.error(e.getMessage());
        return CommonResult.error(GlobalCodeConstants.UN_LOGIN, "用户未登录,请登录!");
    }

}
