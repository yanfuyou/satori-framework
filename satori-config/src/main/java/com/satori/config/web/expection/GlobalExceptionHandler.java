package com.satori.config.web.expection;

import com.alibaba.fastjson2.JSON;
import com.satori.model.code.ReplyCode;
import com.satori.model.exception.ServiceException;
import com.satori.model.result.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YanFuYou
 * @date 03/02/24 下午 10:12
 */

@RestControllerAdvice
@Slf4j
@ResponseStatus(value = HttpStatus.OK)
@Order(3)
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public ResultVO<Object> serviceExceptionHandler(ServiceException e) {
        log.error("业务异常", e);
        return ResultVO.fail(e.getErrorCode(), e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResultVO<Object> exceptionHandler(Exception e) {
        log.error("系统内部错误", e);
        return ResultVO.fail(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ResultVO<String> bindException(BindException ex) {
        ResultVO<String> response;
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!CollectionUtils.isEmpty(fieldErrors)) {
            Map<String, String> errMap = new HashMap<>(fieldErrors.size());
            fieldErrors.forEach(err -> {
                errMap.put(err.getField(), err.getDefaultMessage());
            });
            response = ResultVO.fail(ReplyCode.PARAMETER_FAIL.code, JSON.toJSONString(errMap));
        } else {
            response = ResultVO.fail(ReplyCode.PARAMETER_FAIL.code, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return response;
    }
}
