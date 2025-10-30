package com.smalldragon.yml.context;

import com.smalldragon.yml.context.dto.LoginDTO;
import com.smalldragon.yml.exception.NotLoginException;

/**
 * 用户上下文工具类，用于获取当前登录用户信息
 * @author YML
 */
public class UserContext {

    private static final ThreadLocal<Object> threadLocalCache = new ThreadLocal<>();

    /**
     * 设置当前用户
     * @param currentUser 当前用户对象
     */
    public static void setCurrentUser(Object currentUser) {
        threadLocalCache.set(currentUser);
    }

    /**
     * 获取当前登录用户对象
     * @return 当前登录用户对象
     * @throws NotLoginException 如果用户未登录或用户信息为空
     */
    public static Object getCurrentUser() {
        Object currentUser = threadLocalCache.get();
        if (currentUser == null) {
            throw new NotLoginException("用户未登录或用户信息为空");
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户的 ID
     * @return 当前登录用户的 ID
     * @throws NotLoginException 如果用户未登录或用户信息为空
     */
    public static String getCurrentUserId() {
        Object currentUser = getCurrentUser();
        if (currentUser instanceof LoginDTO) {
            return ((LoginDTO) currentUser).getId();
        }
        throw new NotLoginException("无法获取用户ID，用户对象类型不匹配");
    }

    /**
     * 获取登录用户名（兼容旧调用）
     * @return 当前登录用户的用户名
     * @throws NotLoginException 如果用户未登录或用户信息为空
     */
    public static String getLoginUsername() {
        Object currentUser = getCurrentUser();
        if (currentUser instanceof LoginDTO) {
            return ((LoginDTO) currentUser).getUsername();
        }
        throw new NotLoginException("无法获取用户名，用户对象类型不匹配");
    }

    /**
     * @deprecated 请使用getLoginUsername()替代
     */
    public static String getCurrentUsername() {
        return getLoginUsername();
    }

    /**
     * 清理缓存
     */
    public static void clearCache() {
        threadLocalCache.remove();
    }
}
