package com.satori.env.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

/**
 * 解决 request.getInputStream() 只能读取一次的问题，同时支持 x-www-form-urlencoded 表单参数解析
 *
 * @author YanFuYou
 * @date 2024/02/19 下午 10:56
 */
@Slf4j
public class SatoriRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] payloadBuffer;
    private final Map<String, String> customHeaders;
    private Map<String, String[]> parameterMap;

    public SatoriRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.payloadBuffer = FileCopyUtils.copyToByteArray(request.getInputStream());
        this.customHeaders = new HashMap<>(2);
    }

    public void addHeader(String name, String value) {
        customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String value = customHeaders.get(name);
        return (value != null) ? value : ((HttpServletRequest) getRequest()).getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> names = new HashSet<>(customHeaders.keySet());
        Enumeration<String> originalNames = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (originalNames.hasMoreElements()) {
            names.add(originalNames.nextElement());
        }
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = new ArrayList<>();
        if (customHeaders.containsKey(name)) {
            values.add(customHeaders.get(name));
        }
        Enumeration<String> originalValues = ((HttpServletRequest) getRequest()).getHeaders(name);
        while (originalValues.hasMoreElements()) {
            values.add(originalValues.nextElement());
        }
        return Collections.enumeration(values);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncodingOrUTF8()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(payloadBuffer);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return inputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // Not implemented
            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }

    public String getPayload() throws UnsupportedEncodingException {
        return new String(payloadBuffer, getCharacterEncodingOrUTF8());
    }

    private String getCharacterEncodingOrUTF8() {
        return Optional.ofNullable(getCharacterEncoding()).orElse("UTF-8");
    }

    // ============ 解析 application/x-www-form-urlencoded 表单参数 =============

    @Override
    public String getParameter(String name) {
        ensureParameterMapInitialized();
        String[] values = parameterMap.get(name);
        return (values != null && values.length > 0) ? values[0] : null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        ensureParameterMapInitialized();
        return parameterMap;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        ensureParameterMapInitialized();
        return Collections.enumeration(parameterMap.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        ensureParameterMapInitialized();
        return parameterMap.get(name);
    }

    private void ensureParameterMapInitialized() {
        if (parameterMap != null) return;

        parameterMap = new HashMap<>();

        // 只在 Content-Type 是 application/x-www-form-urlencoded 时解析
        String contentType = getContentType();
        if (contentType != null && contentType.startsWith("application/x-www-form-urlencoded")) {
            try {
                String charset = getCharacterEncodingOrUTF8();
                String body = new String(payloadBuffer, charset);
                String[] pairs = body.split("&");

                for (String pair : pairs) {
                    if (pair.isEmpty()) continue;
                    String[] keyValue = pair.split("=", 2);
                    String key = URLDecoder.decode(keyValue[0], charset);
                    String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], charset) : "";
                    parameterMap.computeIfAbsent(key, k -> {
                        return new String[]{value};
                    });
                }

                // 转换 List<String> 为 String[]

                parameterMap = new HashMap<>(parameterMap);
            } catch (Exception e) {
                log.error("解析表单参数失败", e);
                parameterMap = super.getParameterMap(); // fallback
            }
        } else {
            // 非表单请求直接用原始参数
            parameterMap = super.getParameterMap();
        }
    }
}
