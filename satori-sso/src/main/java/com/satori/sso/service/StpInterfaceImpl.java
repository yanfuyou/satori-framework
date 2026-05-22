package com.satori.sso.service;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.satori.model.constant.RedisKeyConstant;
import com.satori.sso.handler.LoginHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yanfuyou
 * @date 2025/5/24 22:35
 * @description
 **/
public class StpInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object o, String s) {
        List<String> allPermissions = new ArrayList<>(32);
        for (String role : this.getRoleList(o, s)) {
            String permissionsJson = SaManager.getSaTokenDao().get(RedisKeyConstant.PERMISSION_KEY.formatted(LoginHandler.getPlatform(), role));
            if (StringUtils.isBlank(permissionsJson)) {
                continue;
            }
            List<String> permissions = JSON.parseObject(permissionsJson, new TypeReference<ArrayList<String>>() {
            });
            allPermissions.addAll(permissions);
        }
        return allPermissions;
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        Objects.requireNonNull(LoginHandler.getPlatform(), "当前平台未配置[satori.platform]");
        String roles = SaManager.getSaTokenDao().get(RedisKeyConstant.ROLE_KEY.formatted(LoginHandler.getPlatform(), (String) o));
        if (StringUtils.isBlank(roles)){
            return new ArrayList<>();
        }
        return JSON.parseObject(roles, new TypeReference<List<String>>() {
        });
    }

    public void refreshRolePermissions(Map<String, List<String>> rolePermissions) {
        Objects.requireNonNull(LoginHandler.getPlatform(), "当前平台未配置[satori.platform]");
        for (Map.Entry<String, List<String>> entry : rolePermissions.entrySet()) {
            String role = entry.getKey();
            List<String> permissions = entry.getValue();
            SaManager.getSaTokenDao().set(RedisKeyConstant.PERMISSION_KEY.formatted(LoginHandler.getPlatform(), role),
                    JSON.toJSONString(permissions), -1);
        }
    }
}
