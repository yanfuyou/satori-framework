package com.satori.model.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author YanFuYou
 * @date 2024/09/28 16:34
 * @description 分页请求
 */
@Data
public class PageRequest implements Serializable {
    @Schema(description = "页码")
    private Long pageNum = 1L;

    @Schema(description = "页面数量")
    private Long pageSize = 10L;

    @Schema(description = "是否查询总数")
    private Boolean searchCount = true;

    public <T> IPage<T> convert2Page(Class<T> clazz) {
        return new PageInfo<T>(this);
    }
}
