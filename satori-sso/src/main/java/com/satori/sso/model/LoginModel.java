package com.satori.sso.model;

import com.satori.model.enums.ClientEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author YanFuYou
 * @date 2024/09/22 22:23
 * @description 登录数据
 */

@Data
@Accessors(chain = true)
public class LoginModel implements Serializable {
    /**
     * 平台
     */
    private String platform;

    /**
     * 用户信息
     */
    private UserInfo user;

    /**
     * 角色列表
     */
    private List<String> roles;


    /**
     * 权限列表
     */
    private List<String> permissions;

    /**
     * 过期时间
     * 为空永久
     */
    private Long expireTime;


    /**
     * 用户信息
     */
    @Data
    public static class UserInfo implements Serializable{
        /**
         * 用户id
         */
        private Long userId;

        /**
         * openId
         */
        private String openId;

        /**
         * unionId
         */
        private String unionId;

        /**
         * 登录客户端
         */
        private ClientEnum client;

        /**
         * 商户id
         */
        private Long merId;

        /**
         * 用户名
         */
        private String userName;

        /**
         * 昵称
         */
        private String nickName;

        /**
         * 头像
         */
        private String avatar;

        /**
         * 邮箱
         */
        private String email;
    }

}
