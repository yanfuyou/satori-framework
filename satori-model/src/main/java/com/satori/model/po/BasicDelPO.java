package com.satori.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class BasicDelPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6687130973530797350L;

    /**
     * 主键id
     */
    @TableId(
            value = "id",
            type = IdType.AUTO
    )
    private Long id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
