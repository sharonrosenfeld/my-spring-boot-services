package com.sharon.dataaggregator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ApplicationConfig {

    @Autowired
    ApplicationProperties applicationProperties;

    @Bean("APIExecutor")
    public Executor apiExecutor() {
        int apiConcurrency = applicationProperties.getApi_httppool_maxtotal();
        return new ThreadPoolExecutor(1,apiConcurrency,10, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1));
    }

    @Bean
    public Executor siteExecutor() {
        int siteConcurrency = applicationProperties.getSite_httppool_maxtotal();
        return new ThreadPoolExecutor(1,siteConcurrency,10, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1));
    }
}
