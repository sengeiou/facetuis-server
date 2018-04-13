package com.facetuis.server.app.web.basic;

import com.facetuis.server.app.config.FinalValue;
import com.facetuis.server.model.user.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public abstract class UserController {

    protected User getUser(){
        return (User)getRequest().getAttribute(FinalValue.REQUEST_USER);
    }

    private HttpServletRequest getRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
}
