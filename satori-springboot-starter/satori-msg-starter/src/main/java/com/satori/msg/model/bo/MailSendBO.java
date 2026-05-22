package com.satori.msg.model.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class MailSendBO implements Serializable {
    /**
     * 收件人列表
     */
    private List<String> tos;

    /**
     * 主题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    /**
     * 邮件类型
     */
    private boolean html;

    /**
     * 附件
     * 仅可使用url地址
     */
    private List<String> attachments;
}
