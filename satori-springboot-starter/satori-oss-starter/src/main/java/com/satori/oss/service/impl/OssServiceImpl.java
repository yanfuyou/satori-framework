package com.satori.oss.service.impl;

import com.satori.common.util.SignUtils;
import com.satori.model.code.ReplyCode;
import com.satori.oss.model.OssProperties;
import com.satori.oss.model.UploadPolicy;
import com.satori.oss.service.OssService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class OssServiceImpl implements OssService {
    private final OssProperties properties;
    static final String SIGN_FORMATTER = "Signature headers=\"x-date (request-target) host\",keyId=\"%s/%s/%s\",algorithm=\"rsa-sha256\",signature=\"%s\",version=\"1\"";

    @Override
    public UploadPolicy policy(String path, String fileName) {
        String objKey = getKey(path, fileName);
        ZonedDateTime time = LocalDateTime.now().atZone(ZoneId.of("Asia/Shanghai"));
        ZonedDateTime zonedDateTime = time.withZoneSameInstant(ZoneId.of("GMT"));
        String dateStr = zonedDateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME);
        // 暂时只提供上传
        final String method = "put ";
        String target = "/n/" + properties.getNameSpace() + "/b/" + properties.getBucket() + "/o/" + objKey;
        String reqTarget = method + target;

        String signData = "x-date: " + dateStr + "\n"
                + "(request-target): " + reqTarget + "\n"
                + "host: " + properties.getHost();
        String signature;
        try {
            signature = SignUtils.signSha256WithRsa(properties.getPrivateKey(), signData);
        } catch (Exception e) {
            throw ReplyCode.PARAMETER_FAIL.buildEx();
        }

        UploadPolicy res = new UploadPolicy();
        res.setServer(properties.getServer());
        res.setEndpoint(properties.getEndpoint());
        res.setNameSpace(properties.getNameSpace());
        res.setBucket(properties.getBucket());
        res.setHost(properties.getHost());
        res.setDate(dateStr);
        res.setObjKey(objKey);
        String formatted = SIGN_FORMATTER.formatted(properties.getTenantId(), properties.getUserId(), properties.getFingerprint(), signature);
        res.setSignature(formatted);
        return res;
    }

    @Override
    public String authUrl() {
        return properties.getAuthUrl();
    }
}

