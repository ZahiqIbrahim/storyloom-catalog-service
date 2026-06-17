package com.example.storyloom_catalog_service.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class feignConfig {


    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
                100,   // initial interval (ms)
                1000,  // max interval (ms)
                3      // max attempts
        );
    }
}
