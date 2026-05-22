package com.satori.env.advice;

import com.satori.common.util.SatoriThreadLocalUtil;
import com.satori.model.constant.EnvConstant;
import com.satori.model.result.ResultVO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.LocalDateTime;

/**
 * @author yanfuyou
 * @date 2025/08/20 09:59:20
 * @description
 */
@ControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SatoriResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return ResultVO.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ResultVO vo = (ResultVO) body;
        vo.setRequestId(SatoriThreadLocalUtil.get(EnvConstant.TRACE_ID_KEY));
        vo.setRequestTime(SatoriThreadLocalUtil.get(EnvConstant.REQ_TIME_KEY));
        vo.setRequestPath(SatoriThreadLocalUtil.get(EnvConstant.REQ_PATH_KEY));
        return body;
    }
}
