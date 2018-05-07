package com.facetuis.server.admin.web.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {


    private static final Logger logger = Logger.getLogger("后台授权校验");

    @Value("${sys.accesstoken.timeout}")
    private String tokenTimeOut;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("admin ........................");
        return false;
    }

}
