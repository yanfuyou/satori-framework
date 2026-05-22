package com.satori.msg.service;

import cn.hutool.extra.mail.MailException;
import cn.hutool.extra.mail.MailUtil;
import com.satori.msg.model.bo.MailSendBO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MsgService {
    public boolean mailSend(MailSendBO bo) {
        // TODO: 附件解析
        try {
            MailUtil.send(bo.getTos(), bo.getSubject(), bo.getContent(), bo.isHtml());
            return true;
        } catch (MailException e) {
            log.error("邮件发送失败", e);
        }
        return false;
    }
}
