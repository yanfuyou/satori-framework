package com.satori.model.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author YanFuYou
 * @date 2024/02/03 下午 10:41
 */

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public enum ReplyCode implements ICodeService<Object> {
    SUCCESS("00000", "成功", null),
    FAIL("00001", "失败", null),

    LOGIN_EXPIRE("00002", "登录过期", "用户[%s]"),
    PARAMETER_FAIL("00003", "参数错误", "参数错误[%s]"),
    USER_PWD_FAIL("00004", "用户名或密码错误", null),
    ACCOUNT_REPEAT("00005", "账户已存在", "账户已存在[%s]"),
    ACCOUNT_NOT_EXIST("00006", "账户不存在", "账户不存在[%s]"),
    ACCOUNT_SAVE_FAIL("00007", "账户保存失败", null),
    ACCOUNT_UPDATE_FAIL("00008", "账户更新失败", "账户更新失败[%s]"),

    ACCESS_FAIL("00009", "权限异常", "%s"),

    WARNING_FAIL("00010", "警告信息", "%s"),
    REPEAT_REQUEST("00011", "重复请求", "重复请求[%s]"),
    ACCESS_ERROR("00012", "非法请求", "非法请求[%s]"),

    ;

    public final String code;

    public final String desc;

    public final String msgFormat;

}
