package com.satori.sso.model;

/**
 * @author YanFuYou
 * @date 2024/09/22 22:28
 * @description 缓存Key
 */
public interface SSOKey {
    /**
     * 用户前缀
     */
    String USER_PREFIX = "user";

    /**
     * 角色前缀
     */
    String ROLE_PREFIX = "role";

    /**
     * 权限前缀
     */
    String PERMISSION_PREFIX = "permission";
    ;
}
