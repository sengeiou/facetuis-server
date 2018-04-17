package com.facetuis.server.app.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.model.user.User;
import com.facetuis.server.service.user.UserService;
import com.facetuis.server.utils.LoginUtils;
import com.facetuis.server.utils.SysFinalValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = Logger.getLogger("授权校验");

    @Value("${sys.accesstoken.timeout}")
    private String tokenTimeOut;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!HandlerMethod.class.isAssignableFrom(handler.getClass())){
            return true;
        }
        String accessToken = request.getHeader(SysFinalValue.ACCESS_TOKEN);
        String c = request.getHeader(SysFinalValue.CLIENT_TYPE);
        boolean isNeedLogin = LoginUtils.isNeedLogin((HandlerMethod) handler);
        // 判断是否访问的接口是否需要登录
        if(isNeedLogin){
            logger.info("当前请求需要登录" );
            boolean loginResult = false;
            if(!StringUtils.isEmpty(accessToken)){
                loginResult = doLogin(accessToken, request, response);
                if(!loginResult){
                    return false;
                }
            }else{
                authFail(response,new BaseResponse());
                return false;
            }
            return loginResult;
        }else{
            logger.info("当前请求不需要登录" );
            return true;
        }
    }

    private boolean doLogin(String token,HttpServletRequest request, HttpServletResponse response) throws IOException {
        BaseResponse result = new BaseResponse();
        User user = userService.getUserByToken(token);
        if (user == null) {
            return authFail(response, result);
        }
        if(!StringUtils.isEmpty(tokenTimeOut)){
            long loginTime = user.getLoginTime().getTime();
            long now = System.currentTimeMillis();
            long timeOut = Integer.parseInt(tokenTimeOut);
            if((now - loginTime) >= timeOut){
                return tokenTimeOut(response, result);
            }
        }
        request.setAttribute(SysFinalValue.USER_PARAMTER_KEY, user);
        logger.info("当前登录用户信息：");
        logger.info("UUID：" + user.getUuid());
        logger.info("手机号：" + user.getMobileNumber());
        logger.info("AccessToken：" + user.getToken());
        return true;
    }

    /**
     * 身份校验失败
     * @param response
     * @param result
     * @return
     * @throws IOException
     */
    private boolean authFail(HttpServletResponse response, BaseResponse result) throws IOException {
        result.setCode(100);
        result.setMessage("Token验证失败");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().flush();
        response.getWriter().print(JSONObject.toJSON(result));
        return false;
    }

    /**
     * Token过期
     * @param response
     * @param result
     * @return
     * @throws IOException
     */
    private boolean tokenTimeOut(HttpServletResponse response, BaseResponse result) throws IOException {
        logger.info("Token过期");
        result.setCode(100);
        result.setMessage("Token已过期，请重新登录");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().flush();
        response.getWriter().print(JSONObject.toJSON(result));
        return false;
    }
}
