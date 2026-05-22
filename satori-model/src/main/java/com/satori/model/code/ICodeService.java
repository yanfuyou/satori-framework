package com.satori.model.code;


import com.satori.model.exception.ServiceException;
import com.satori.model.result.ResultVO;

import java.util.Optional;

/**
 * @author YanFuYou
 * @date 2024/02/03 下午 10:51
 * 错误码接口
 */


public interface ICodeService<T> {
    String code();

    String desc();

    String msgFormat();

    default ServiceException buildEx() {
        return new ServiceException(code(), desc());
    }

    default ServiceException buildEx(Object... args) {
        return new ServiceException(code(), genMsg(args));
    }

    default ResultVO<T> buildResult(T record) {
        return new ResultVO<>(this.code(), this.desc(), record);
    }

    default String genMsg(Object... args) {
        return Optional.ofNullable(msgFormat())
                .orElse(desc())
                .formatted(args);
    }
}
