package com.example.imagehost.config;


import com.example.imagehost.interceptor.JWTInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JWTInterceptorConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/identify")
                .excludePathPatterns("/auth/register")
                .excludePathPatterns("/auth/login")
                .excludePathPatterns("/image/download/**");
    }
}
