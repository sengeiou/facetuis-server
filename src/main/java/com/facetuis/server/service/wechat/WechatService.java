package com.facetuis.server.service.wechat;

import com.alibaba.fastjson.JSONObject;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.wechat.vo.AccessTokenResponse;
import com.facetuis.server.app.web.basic.BaseResponse;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
public class WechatService {

    @Value("${wechat.access.token.url}")
    private String GET_TOKEN_URL ;
    @Value("${wechat.app.id}")
    private String WECHAT_APPID;
    @Value("${wechat.app.secret}")
    private String WECHAT_SECRET;

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
        }else{
            result.setCode(600);
            result.setMessage("业务处理失败，请稍后再试！");
        }
        return result;
    }

}
