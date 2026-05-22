package com.satori.oss.service;

import cn.hutool.core.util.IdUtil;
import com.satori.oss.model.UploadPolicy;

public interface OssService {
    /**
     * 获取上传策略
     */
    UploadPolicy policy(String path, String fileName);

    /**
     * 访问配置
     */
    String authUrl();

    default String getKey(String path, String fileName) {
        int idx = fileName.lastIndexOf(".");
        String suffix = "";
        if (idx != -1 && idx != fileName.length() - 1) {
            suffix = fileName.substring(idx);
        }
        String type = "/" + suffix.replace(".", "") + "/";
        return path + type + IdUtil.simpleUUID() + suffix;
    }
}
