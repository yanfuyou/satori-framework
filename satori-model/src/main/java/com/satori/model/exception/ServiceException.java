package com.satori.model.exception;

import com.satori.model.exception.enums.ExceptionTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author YanFuYou
 * @date 2024/02/03 下午 10:58
 * 业务异常
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5841071414321907182L;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 错误类型
     */
    private ExceptionTypeEnum exceptionType;

    public ServiceException(String errorCode, String errorMessage) {
        this(errorCode, errorMessage, ExceptionTypeEnum.ERROR);
    }

    public ServiceException(String errorCode, String errorMessage, ExceptionTypeEnum exceptionType) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.exceptionType = exceptionType;
    }

    public ServiceException(String errorCode, String errorMessage, Throwable cause) {
        this(errorCode, errorMessage, cause, ExceptionTypeEnum.ERROR);
    }

    public ServiceException(String errorCode, String errorMessage, Throwable cause, ExceptionTypeEnum exceptionType) {
        super(cause.getMessage(), cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.exceptionType = exceptionType;
    }
}
