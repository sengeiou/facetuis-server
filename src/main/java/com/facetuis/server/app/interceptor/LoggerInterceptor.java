package com.facetuis.server.app.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("request URL :: " + request.getRequestURI());
        System.out.println("request URL :: " + request.getRequestURL());
        System.out.println("request URL :: " + request.getQueryString());
        return true;
    }
}
