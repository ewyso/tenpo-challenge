package com.challenge.tenpo.config;

import com.challenge.tenpo.adapter.cache.model.ResultCacheModel;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Value("${cache.ttl}") private Long ttl;

    @Bean("resultCache")
    public Cache<String, ResultCacheModel> cacheFactory(){
        return CacheBuilder.newBuilder()
                .expireAfterWrite(ttl, TimeUnit.MILLISECONDS)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }
}
