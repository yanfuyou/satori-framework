package com.satori.oss.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UploadPolicy {
    private String server;

    private String endpoint;

    private String nameSpace;

    private String bucket;

    private String host;

    private String objKey;

    private String date;

    private String secretId;

    private String keyTime;

    private String policyBase64;

    private String signature;

    private String previewUrl;
}
