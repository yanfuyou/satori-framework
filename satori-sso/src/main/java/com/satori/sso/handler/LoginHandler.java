package com.satori.sso.handler;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.satori.model.code.ReplyCode;
import com.satori.model.constant.RedisKeyConstant;
import com.satori.sso.model.LoginModel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author YanFuYou
 * @date 2024/09/22 22:00
 * @description 登录处理器
 */
public class LoginHandler {
    @Getter
    private static String platform;

    public static void initPlatform(String platform) {
        LoginHandler.platform = platform;
    }

    /**
     * 登录
     */
    public static String login(LoginModel dto) {
        Objects.requireNonNull(dto.getUser().getUserId(), "用户id不能为空");
        Objects.requireNonNull(platform, "请先初始化登录处理器");
        if (Objects.nonNull(dto.getExpireTime())) {
            StpUtil.login(dto.getUser().getUserId(), dto.getExpireTime());
        } else {
            StpUtil.login(dto.getUser().getUserId());
        }
        if (Objects.nonNull(dto.getUser())) {
            SaManager.getSaTokenDao().set(RedisKeyConstant.USER_KEY.formatted(platform, dto.getUser().getUserId()), JSON.toJSONString(dto.getUser()), StpUtil.getTokenTimeout());
        }
        if (!CollectionUtils.isEmpty(dto.getRoles())) {
            SaManager.getSaTokenDao().set(RedisKeyConstant.ROLE_KEY.formatted(platform, dto.getUser().getUserId()), JSON.toJSONString(dto.getRoles()), StpUtil.getTokenTimeout());
        }
        if (!CollectionUtils.isEmpty(dto.getPermissions())) {
            SaManager.getSaTokenDao().set(RedisKeyConstant.PERMISSION_KEY.formatted(platform, dto.getUser().getUserId()), JSON.toJSONString(dto.getPermissions()), StpUtil.getTokenTimeout());
        }
        return StpUtil.getTokenValue();
    }

    /**
     * 用户信息
     */
    public static LoginModel.UserInfo getCurrentUser() {
        Objects.requireNonNull(platform, "请先初始化登录处理器");
        try {
            StpUtil.checkLogin();
        } catch (NotLoginException e) {
            throw ReplyCode.LOGIN_EXPIRE.buildEx();
        }
        String userJson = SaManager.getSaTokenDao().get(RedisKeyConstant.USER_KEY.formatted(platform, StpUtil.getLoginId()));
        if (StringUtils.isBlank(userJson)) {
            throw ReplyCode.LOGIN_EXPIRE.buildEx();
        }
        return JSON.parseObject(userJson, LoginModel.UserInfo.class);
    }

    /**
     * 获取用户指定用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    public static LoginModel.UserInfo getUser(Long userId){
        if (userId == null){
            return getCurrentUser();
        }
        String userJson = SaManager.getSaTokenDao().get(RedisKeyConstant.USER_KEY.formatted(platform, userId));
        if (StringUtils.isBlank(userJson)) {
            throw ReplyCode.LOGIN_EXPIRE.buildEx();
        }
        return JSON.parseObject(userJson, LoginModel.UserInfo.class);
    }

    public static List<String> getRoles() {
        Objects.requireNonNull(platform, "请先初始化登录处理器");
        return StpUtil.getRoleList();
    }

    public static List<String> getPermissions() {
        Objects.requireNonNull(platform, "请先初始化登录处理器");
        return StpUtil.getPermissionList();
    }

    /**
     * 用户id
     */
    public static Long getCurrentUserId() {
        return StpUtil.getLoginIdAsLong();
    }

    public static boolean isLogin() {
        Objects.requireNonNull(platform, "请先初始化登录处理器");
        return StpUtil.isLogin();
    }

    /**
     * 刷新用户信息
     */
    public static void refreshUserInfo(Long userId, LoginModel.UserInfo userInfo) {
        try {
            userId = userId == null ? getCurrentUserId() : userId;
        } catch (Exception e) {
            return;
        }
        long tokenTimeout = StpUtil.getTokenTimeout(StpUtil.getTokenValueByLoginId(userId));
        if (Objects.nonNull(userInfo)) {
            StpUtil.getLoginId();
            SaManager.getSaTokenDao().set(RedisKeyConstant.USER_KEY.formatted(platform, userId), JSON.toJSONString(userInfo), tokenTimeout);
        }
    }

    /**
     * 退出
     */
    public static void logout() {
        Objects.requireNonNull(platform, "请先初始化登录处理器");
        long loginId;
        try {
            loginId = StpUtil.getLoginIdAsLong();
        } catch (NotLoginException e) {
            return;
        }
        StpUtil.logout();
        SaManager.getSaTokenDao().delete(RedisKeyConstant.USER_KEY.formatted(platform, loginId));
        SaManager.getSaTokenDao().delete(RedisKeyConstant.ROLE_KEY.formatted(platform, loginId));
    }
}
