package com.satori.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 操作记录
 *
 * @author YanFuYou
 * @date 2024/02/13 下午 11:28
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class OperationInputPO extends LongInputPO {
    @Serial
    private static final long serialVersionUID = -1025065778634477535L;

    /**
     * 创建者id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 修改者id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long modifierId;
}
