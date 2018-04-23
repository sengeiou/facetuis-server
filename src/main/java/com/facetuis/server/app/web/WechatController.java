package com.facetuis.server.app.web;

import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.wechat.WechatService;
import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import com.facetuis.server.app.web.request.WechatTokenRequest;
import com.facetuis.server.app.web.response.WechatTokenResponse;
import com.facetuis.server.service.wechat.response.MpGetUserInfoResponse;
import com.facetuis.server.service.wechat.vo.AccessTokenResponse;
import com.facetuis.server.utils.PayCommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/1.0/wechat")
public class WechatController extends FacetuisController {

    @Value("${wechat.mp.token}")
    private String token;
    @Value("${sys.web.register.url}")
    private String webRegister;

    @Autowired
    private WechatService wechatService;


    /**
     * 微信公众号token验证
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @RequestMapping(value = "/mp/token",method = RequestMethod.GET)
    public String checkToken(String signature,String timestamp,String nonce,String echostr){
        boolean b = PayCommonUtil.checkSignature(signature, timestamp, nonce, token);
        if(b){
            return echostr;
        }
        return "";
    }

    /**
     *
     * 获取微信公众号code,重定向接受code
     * @param recommder
     * @param code
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/mp/userinfo/{recommder}",method = RequestMethod.GET)
    public void getCode(@PathVariable String recommder,String code,  HttpServletResponse response) throws IOException {
        BaseResult<AccessTokenResponse> token = wechatService.getToken(code);
        if(!token.hasError()){
            AccessTokenResponse tokenResponse = new AccessTokenResponse();
            String location = String.format(webRegister,tokenResponse.getOpenid(),tokenResponse.getAccess_token(),recommder);
            // 重定向到网页注册页面
            response.sendRedirect(location);
        }
    }

    /**
     * APP微信登陆 根据code获取accessToken
     * @param request
     * @param result
     * @return
     */
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
