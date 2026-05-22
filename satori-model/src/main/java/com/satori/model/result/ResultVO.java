package com.satori.model.result;

import com.satori.model.code.ReplyCode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author YanFuYou
 * @date 2024/02/03 下午 10:18
 */

@Data
@Accessors(chain = true)
public class ResultVO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 4003788586327474197L;

    private String code;

    private String message;

    private Boolean success;

    private T data;

    /** auto gen **/
    private String requestId;
    private LocalDateTime requestTime;
    private String requestPath;

    public ResultVO(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = ReplyCode.SUCCESS.code().equals(code);
    }

    public ResultVO(String code, String message) {
        this.code = code;
        this.message = message;
        this.success = ReplyCode.SUCCESS.code().equals(code);
    }

    public ResultVO(T data) {
        this(ReplyCode.SUCCESS.code(), ReplyCode.SUCCESS.desc(), data);
    }

    public ResultVO() {
        this(ReplyCode.SUCCESS.code(), ReplyCode.SUCCESS.desc());
    }

    public static <T> ResultVO<T> success(String code, String message, T record) {
        return new ResultVO<>(code, message, record);
    }

    public static <T> ResultVO<T> success(T record) {
        return new ResultVO<>(record);
    }

    public static <T> ResultVO<T> success() {
        return new ResultVO<>(null);
    }

    public static <T> ResultVO<T> fail(String code, String message, T record) {
        return new ResultVO<>(code, message, record);
    }

    public static <T> ResultVO<T> fail(String code, String message) {
        return new ResultVO<>(code, message);
    }

    public static <T> ResultVO<T> fail(String message) {
        return new ResultVO<>(ReplyCode.FAIL.code(), message);
    }

    public static <T> ResultVO<T> fail() {
        return new ResultVO<>(ReplyCode.FAIL.code(), ReplyCode.FAIL.desc());
    }
}
