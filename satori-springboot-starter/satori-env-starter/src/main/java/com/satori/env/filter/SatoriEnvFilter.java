package com.satori.env.filter;


import cn.hutool.core.util.IdUtil;
import com.satori.common.util.SatoriThreadLocalUtil;
import com.satori.env.config.SatoriEnvProperties;
import com.satori.env.wrapper.SatoriRequestWrapper;
import com.satori.model.constant.EnvConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author YanFuYou
 * @date 2024/02/20 下午 10:51
 */
@AllArgsConstructor
@Slf4j
public class SatoriEnvFilter extends OncePerRequestFilter {
    private final SatoriEnvProperties envProperties;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        addResponseHeader(response);
        String uri = request.getRequestURI();
        HttpServletRequest requestWrapper = request;
        if (!(request instanceof SatoriRequestWrapper)) {
            requestWrapper = new SatoriRequestWrapper(request);
        }

        HttpServletResponse responseWrapper = response;
        if (!(response instanceof ContentCachingResponseWrapper)) {
            responseWrapper = new ContentCachingResponseWrapper(response);
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start(uri);
        beforeRequest((SatoriRequestWrapper) requestWrapper);
        try {
            log.info("请求开始uri:{}", uri);
            if (HttpMethod.OPTIONS.matches(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.flushBuffer();
                return;
            }
            filterChain.doFilter(requestWrapper, responseWrapper);
            stopWatch.stop();
            long respTime = stopWatch.getTotalTimeMillis();
            afterRequest(uri, respTime, responseWrapper);
        } finally {
            SatoriThreadLocalUtil.clear();
            MDC.clear();
        }
    }


    /**
     * 可以记录请求参数
     */
    private void beforeRequest(SatoriRequestWrapper wrapper) {
        String traceId = IdUtil.simpleUUID();
        SatoriThreadLocalUtil.put(EnvConstant.TRACE_ID_KEY, traceId);
        SatoriThreadLocalUtil.putReqSource(envProperties.getReqSource());
        SatoriThreadLocalUtil.put(EnvConstant.REQ_TIME_KEY, LocalDateTime.now());
        SatoriThreadLocalUtil.put(EnvConstant.REQ_PATH_KEY, wrapper.getRequestURI());
        MDC.put(EnvConstant.TRACE_ID_KEY, traceId);
    }

    /**
     * 可以记录响应参数
     */
    private void afterRequest(String uri, Long respTime, HttpServletResponse responseWrapper) throws IOException {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(responseWrapper, ContentCachingResponseWrapper.class);
        if (!Objects.isNull(wrapper)) {
            wrapper.copyBodyToResponse();
            log.info("请求结束uri:{},耗时:{} ms;", uri, respTime);
        }

    }

    private void addResponseHeader(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
    }
}
