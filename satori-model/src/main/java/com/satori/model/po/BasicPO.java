package com.satori.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 默认字段
 *
 * @author YanFuYou
 * @date 2024/02/13 上午 12:36
 */

@Data
public class BasicPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6687130973530797350L;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
