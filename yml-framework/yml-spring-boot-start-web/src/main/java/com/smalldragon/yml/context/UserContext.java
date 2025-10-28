package com.smalldragon.yml.context;

import com.smalldragon.yml.context.dto.LoginDTO;

/**
 * 用户上下文工具类，用于获取当前登录用户信息
 * @author YML
 */
public class UserContext {

    private static final ThreadLocal<LoginDTO> threadLocalCache = new ThreadLocal<>();


    public static void setLoginUser(LoginDTO loginUser) {
        threadLocalCache.set(loginUser);
    }


    /**
     * 获取当前登录用户的 LoginVO 对象
     *
     * @return 当前登录用户的 LoginVO 对象
     * @throws RuntimeException 如果用户未登录或用户信息为空
     */
    public static LoginDTO getLoginUser() {
        // 先从ThreadLocal中获取
        LoginDTO loginUser = threadLocalCache.get();
        if (loginUser != null) {
            return loginUser;
        }

        // 如果ThreadLocal中没有，则从Sa-Token的Session中获取
        Object userInfo = StpUtil.getTokenSession().get("userInfo");
        if (userInfo == null) {
            throw new RuntimeException("用户未登录或用户信息为空");
        }

        loginUser = (LoginDTO) userInfo;
        // 将获取到的用户信息存入ThreadLocal，避免重复从Session中获取
        threadLocalCache.set(loginUser);
        return loginUser;
    }

    /**
     * 获取当前登录用户的 ID
     *
     * @return 当前登录用户的 ID
     * @throws RuntimeException 如果用户未登录或用户信息为空
     */
    public static String getLoginUserId() {
        return getLoginUser().getId();
    }

    /**
     * 获取当前登录用户的用户名
     *
     * @return 当前登录用户的用户名
     * @throws RuntimeException 如果用户未登录或用户信息为空
     */
    public static String getLoginUsername() {
        return getLoginUser().getUsername();
    }

    public static void clearCache() {
        threadLocalCache.remove();
    }

}
