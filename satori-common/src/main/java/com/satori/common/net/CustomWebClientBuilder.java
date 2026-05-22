package com.satori.common.net;

import io.netty.channel.ChannelOption;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * @author YanFuYou
 * @date 2025/07/11 11:09
 * @description
 */

public class CustomWebClientBuilder {
    /** 设置最大并发连接数，每个主机名+端口的连接池上限 */
    private int maxConnections = 64;
    /** 设置最大等待获取连接的请求数，可以理解为等待队列 */
    private int pendingAcquireMaxCount = 500;
    /** 设置获取连接的最大等待时间 */
    private Duration pendingAcquireTimeout = Duration.ofMillis(3000);
    /** 设置连接空闲时间，减少不必要的资源消耗 */
    private Duration maxIdleTime = Duration.ofSeconds(60);
    /** 设置连接最大生命周期，确保连接定期关闭和重新建立，以防止长时间使用的连接出现问题 */
    private Duration maxLifeTime = Duration.ofMinutes(10);
    /** 建立连接超时时间。快速失败，避免长时间等待连接建立。 */
    private int connectTimeoutMillis = 2000;
    /** 响应超时时间。确保在合理时间内获取响应，避免长时间等待。 */
    private Duration responseTimeout = Duration.ofSeconds(30);
    private String baseUrl;
    private boolean wiretap = false;
    /** 缓冲池配置，All codecs are limited to 256K by default. */
    private ExchangeStrategies exchangeStrategies;

    public CustomWebClientBuilder setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    public CustomWebClientBuilder setPendingAcquireMaxCount(int pendingAcquireMaxCount) {
        this.pendingAcquireMaxCount = pendingAcquireMaxCount;
        return this;
    }

    public CustomWebClientBuilder setPendingAcquireTimeout(Duration pendingAcquireTimeout) {
        this.pendingAcquireTimeout = pendingAcquireTimeout;
        return this;
    }

    public CustomWebClientBuilder setMaxIdleTime(Duration maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
        return this;
    }

    public CustomWebClientBuilder setMaxLifeTime(Duration maxLifeTime) {
        this.maxLifeTime = maxLifeTime;
        return this;
    }

    public CustomWebClientBuilder setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        return this;
    }

    public CustomWebClientBuilder setResponseTimeout(Duration responseTimeout) {
        this.responseTimeout = responseTimeout;
        return this;
    }

    public CustomWebClientBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public CustomWebClientBuilder setWiretap(boolean wiretap) {
        this.wiretap = wiretap;
        return this;
    }

    public CustomWebClientBuilder setExchangeStrategies(ExchangeStrategies exchangeStrategies) {
        this.exchangeStrategies = exchangeStrategies;
        return this;
    }

    public WebClient build() {
        ConnectionProvider provider = ConnectionProvider.builder("customConnectionPool")
                .maxConnections(maxConnections)
                .pendingAcquireMaxCount(pendingAcquireMaxCount)
                .pendingAcquireTimeout(pendingAcquireTimeout)
                .maxIdleTime(maxIdleTime)
                .maxLifeTime(maxLifeTime)
                .build();

        HttpClient httpClient = HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMillis)
                .responseTimeout(responseTimeout);
        if (baseUrl != null) {
            httpClient = httpClient.baseUrl(baseUrl);
        }
        if (wiretap) {
            httpClient = httpClient.wiretap(true);
        }

        WebClient.Builder builder = WebClient.builder();
        if (baseUrl != null) {
            builder.baseUrl(baseUrl);
        }
        if (exchangeStrategies != null) {
            builder.exchangeStrategies(exchangeStrategies);
        }
        return builder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public static CustomWebClientBuilder create() {
        return new CustomWebClientBuilder();
    }
}

