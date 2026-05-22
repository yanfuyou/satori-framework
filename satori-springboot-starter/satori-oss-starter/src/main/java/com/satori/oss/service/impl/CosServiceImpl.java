package com.satori.oss.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.satori.oss.model.OssProperties;
import com.satori.oss.model.UploadPolicy;
import com.satori.oss.service.OssService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanfuyou
 * @date 2025/05/28 19:35:00
 * @description 文档路径 https://cloud.tencent.com/document/product/436/118723
 */
@RequiredArgsConstructor
@Slf4j
public class CosServiceImpl implements OssService {
    @Resource
    private OssProperties ossProperties;

    public static final String SERVER = "cos";

    @Override
    public UploadPolicy policy(String path, String fileName) {
        String key = getKey(path, fileName);
        LocalDateTime now = LocalDateTime.now();
        String expiration = now.plusMinutes(5)
                .atZone(ZoneId.of("Asia/Shanghai"))
                .format(java.time.format.DateTimeFormatter.ISO_INSTANT);
        long startTimestamp = now
                .atZone(ZoneId.of("Asia/Shanghai"))
                .toInstant()
                .getEpochSecond();
        long endTimestamp = startTimestamp + 60 * 5;
        List<Map<String, Object>> conditions = new ArrayList<>(3);
        Map<String, Object> bucket = Map.of("bucket", ossProperties.getBucket());
        conditions.add(bucket);
        Map<String, Object> algorithm = Map.of("q-sign-algorithm", "sha1");
        conditions.add(algorithm);
        Map<String, Object> secretId = Map.of("q-ak", ossProperties.getSecretId());
        conditions.add(secretId);
        String keyTime = String.format("%s;%s", startTimestamp, endTimestamp);
        Map<String, Object> signTime = Map.of("q-sign-time", keyTime);
        conditions.add(signTime);
        Map<String, Object> policy = new HashMap<>(2);
        policy.put("expiration", expiration);
        policy.put("conditions", conditions);
        String policyStr = JSON.toJSONString(policy);
        String signKey = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, ossProperties.getSecretKey()).hmacHex(keyTime);
        String stringToSign = DigestUtil.sha1Hex(policyStr);
        String signature = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, signKey).hmacHex(stringToSign);


        return new UploadPolicy()
                .setServer(ossProperties.getServer())
                .setSecretId(ossProperties.getSecretId())
                .setHost(ossProperties.getHost())
                .setEndpoint(ossProperties.getEndpoint())
                .setBucket(ossProperties.getBucket())
                .setObjKey(key)
                .setKeyTime(keyTime)
                .setPolicyBase64(Base64.encode(policyStr))
                .setSignature(signature)
                .setPreviewUrl(ossProperties.getAuthUrl() + "/" + key);
    }

    @Override
    public String authUrl() {
        return ossProperties.getAuthUrl();
    }
}
