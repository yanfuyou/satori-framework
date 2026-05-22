package com.satori.model.constant;

/**
 * redis key
 *
 * @author YanFuYou
 * @date 2024/03/03 下午 04:28
 */
public interface RedisKeyConstant {

    /**
     * 冒号分隔符
     */
    String SEPARATOR_COLON = ":";

    /**
     * 用户信息
     */
    String USER_KEY = "%s:user:%s";

    /**
     * 角色权限
     */
    String PERMISSION_KEY = "%s:permissions:%s";

    /**
     * 用户角色
     */
    String ROLE_KEY = "%s:roles:%s";
}
