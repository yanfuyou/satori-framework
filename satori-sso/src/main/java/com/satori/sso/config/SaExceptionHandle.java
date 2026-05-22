package com.satori.sso.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.satori.model.code.ReplyCode;
import com.satori.model.result.ResultVO;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SaExceptionHandle {

    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResultVO<?> notLoginException(NotLoginException ex) {
        return ReplyCode.LOGIN_EXPIRE.buildResult(null);
    }


    @ExceptionHandler({NotPermissionException.class, NotRoleException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResultVO<?> notPermission(Exception ex) {
        return ReplyCode.ACCESS_FAIL.buildResult(null);
    }
}
