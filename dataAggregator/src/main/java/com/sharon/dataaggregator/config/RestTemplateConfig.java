package com.sharon.dataaggregator.config;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Autowired
    ApplicationProperties applicationProperties;

    @Bean("apiRestTemplate")
    public RestTemplate restTemplateAPI() {
        HttpComponentsClientHttpRequestFactory f = new HttpComponentsClientHttpRequestFactory(apiHttpClient());
        return new RestTemplate(f);
    }

    @Bean("siteRestTemplate")
    public RestTemplate restTemplateSite() {
        HttpComponentsClientHttpRequestFactory f = new HttpComponentsClientHttpRequestFactory(siteHttpClient());
        return new RestTemplate(f);
    }


    @Bean
    public HttpClient apiHttpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(applicationProperties.getApi_httppool_maxtotal());
        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .build();
    }

    @Bean
    public HttpClient siteHttpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(applicationProperties.getSite_httppool_maxtotal());
        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .build();
    }
}///