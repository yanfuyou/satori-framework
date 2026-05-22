package com.satori.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 自增id
 *
 * @author YanFuYou
 * @date 2024/02/13 下午 11:24
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class LongInputPO extends BasicPO {

    @Serial
    private static final long serialVersionUID = -7783130733460339609L;


    /**
     * 主键id
     */
    @TableId(
            value = "id",
            type = IdType.INPUT
    )
    private Long id;
}
