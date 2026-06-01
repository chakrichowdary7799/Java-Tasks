package com.example.apigateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.apigateway.interceptor.ApiGatewayInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApiGatewayInterceptor gatewayInterceptor;

   public WebMvcConfig(ApiGatewayInterceptor gatewayInterceptor) {
        this.gatewayInterceptor = gatewayInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(gatewayInterceptor).addPathPatterns("/api/**");
    }
}