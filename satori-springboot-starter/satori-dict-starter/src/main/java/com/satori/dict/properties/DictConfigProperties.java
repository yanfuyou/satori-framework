package com.satori.dict.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @author cat_y
 * @description
 * @date 2024/08/04 00:22
 */

@Data
@ConfigurationProperties(prefix = "satori.dict")
public class DictConfigProperties implements Serializable {
    private List<String> paths;

    private String scope;

    private Boolean enableEndpoint;
}
