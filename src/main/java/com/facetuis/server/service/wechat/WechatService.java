package com.facetuis.server.service.wechat;

import com.alibaba.fastjson.JSONObject;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.wechat.response.MpGetUserInfoResponse;
import com.facetuis.server.service.wechat.vo.AccessTokenResponse;
import com.facetuis.server.app.web.basic.BaseResponse;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class WechatService {

    private static final Logger logger = Logger.getLogger(WechatService.class.getName());

    @Value("${wechat.access.token.url}")
    private String GET_TOKEN_URL ;
    @Value("${wechat.access.token.refresh.url}")
    private String refreshUrl;
    @Value("${wechat.app.id}")
    private String WECHAT_APPID;
    @Value("${wechat.app.secret}")
    private String WECHAT_SECRET;
    @Value("${wechat.mp.user.info}")
    private String GET_USER_INFO;


    // 根据CODE获取AccessToken
    public  BaseResult<AccessTokenResponse> getToken(String code){
        BaseResult result = new BaseResult();
        String  url = String.format(GET_TOKEN_URL + "?appid=%s&secret=%s&code=%s&grant_type=authorization_code",WECHAT_APPID,WECHAT_SECRET,code);
        String response = null;
        try{
            response =  Request.Get(url).execute().returnContent().asString();
        }catch (IOException e){
            e.printStackTrace();
            result.setCode(500);
            result.setMessage(e.getMessage());
            return result;
        }
        if(!StringUtils.isEmpty(response)){
            AccessTokenResponse accessTokenResponse = JSONObject.parseObject(response, AccessTokenResponse.class);
            result.setResult(accessTokenResponse);
            refreshToken(accessTokenResponse.getRefresh_token());
        }else{
            result.setCode(600);
            result.setMessage("业务处理失败，请稍后再试！");
        }
        return result;
    }

    public BaseResult<MpGetUserInfoResponse > getUserInfo(String access_token,String openid){
        BaseResult<MpGetUserInfoResponse> result = new BaseResult<>();
        String  url = String.format(GET_USER_INFO,access_token,openid);
        String response = null;
        try{
            response =  Request.Get(url).execute().returnContent().asString();
            if(!StringUtils.isEmpty(response)){
                MpGetUserInfoResponse accessTokenResponse = JSONObject.parseObject(response, MpGetUserInfoResponse.class);
                result.setResult(accessTokenResponse);
            }else{
                result.setCode(600);
                result.setMessage("业务处理失败，请稍后再试！");
            }
            return result;
        }catch (IOException e){
            e.printStackTrace();
            return new BaseResult(600,e.getMessage());
        }
    }


    public void refreshToken(String refreshToken){
        String  url = String.format(refreshUrl + "?appid=%s&secret=%s&grant_type=refresh_token&refresh_token=%s",WECHAT_APPID,WECHAT_SECRET,refreshToken);
        String response = null;
        try{
            response =  Request.Get(url).execute().returnContent().asString();
            logger.info("刷新ACCESS_TOKEN" + response);
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
    }

}
