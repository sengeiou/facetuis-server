package com.facetuis.server.app.web;

import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.wechat.WechatService;
import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import com.facetuis.server.app.web.request.WechatTokenRequest;
import com.facetuis.server.app.web.response.WechatTokenResponse;
import com.facetuis.server.service.wechat.vo.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/1.0/wechat")
public class WechatController extends FacetuisController {

    @Autowired
    private WechatService wechatService;


    @RequestMapping(value = "/token",method = RequestMethod.POST)
    public BaseResponse getToken(@RequestBody @Valid WechatTokenRequest request , BindingResult result){
        if(result.hasErrors()){
            return erroorResult(result);
        }
        WechatTokenResponse wechatTokenResponse = new WechatTokenResponse();
        BaseResult<AccessTokenResponse> baseResult = wechatService.getToken(request.getCode());
        if(baseResult.hasError()){
            return onResult(baseResult);
        }
        AccessTokenResponse response = baseResult.getResult();
        wechatTokenResponse.setAccess_token(response.getAccess_token());
        wechatTokenResponse.setOpenid(response.getOpenid());
        return successResult(wechatTokenResponse);
    }

}
