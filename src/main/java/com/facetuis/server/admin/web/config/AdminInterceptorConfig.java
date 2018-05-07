package com.facetuis.server.admin.web.config;

import com.facetuis.server.admin.web.interceptor.AdminAuthInterceptor;
import com.facetuis.server.app.interceptor.AuthInterceptor;
import com.facetuis.server.app.interceptor.LoggerInterceptor;
import com.facetuis.server.app.interceptor.SignInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdminInterceptorConfig implements WebMvcConfigurer {

    @Bean
    AdminAuthInterceptor adminAuthInterceptor(){
        return new AdminAuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(adminAuthInterceptor())
                .excludePathPatterns("/error")
                .excludePathPatterns("/static/*")
                .addPathPatterns("/admin/**");


    }
}
