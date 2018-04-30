package com.facetuis.server.app.web;

import com.alipay.api.AlipayClient;
import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import com.facetuis.server.app.web.request.LoginRequest;
import com.facetuis.server.model.mobile.SmsModelCode;
import com.facetuis.server.model.user.User;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.sms.SmsService;
import com.facetuis.server.service.user.UserService;
import com.facetuis.server.utils.CaptchafcUtil;
import com.facetuis.server.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.facetuis.server.utils.SysFinalValue.SESSION_CAPTCHA_ID;

@RestController
@RequestMapping("/1.0/login")
public class LoginController extends FacetuisController {

    @Autowired
    private UserService userService;
    @Autowired
    private SmsService smsService;

    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse login(@RequestBody LoginRequest request) {

        User mobileUser = null;
        User wechatUser = null;
        // 根据手机号获取用户
        if (!StringUtils.isEmpty(request.getMobile_number())) {
            mobileUser = userService.findByMobile(request.getMobile_number());
        }
        // 根据openid获取用户
        if (!StringUtils.isEmpty(request.getOpenid())) {
            wechatUser = userService.findByOpenId(request.getOpenid());
        }
        // 登录
        if (mobileUser != null && wechatUser != null) {
            if (mobileUser.getUuid().equals(wechatUser.getUuid())) {
                User user = userService.login(mobileUser.getUuid());
                return successResult(user);
            } else {
                return new BaseResponse(600, "手机用户和微信用户不匹配，登录失败");
            }
        }

        // 首先绑定手机号码 第一次请求
        if ( wechatUser == null &&  mobileUser == null  && StringUtils.isEmpty(request.getOpenid())  && !StringUtils.isEmpty(request.getMobile_number())) {
            if (!StringUtils.isEmpty(request.getVerification_code())) {
                BaseResult baseResult = smsService.checkCode(request.getMobile_number(), request.getVerification_code(), SmsModelCode.LOGIN,false);
                if (baseResult.hasError()) {
                    return onResult(baseResult);
                }
                userService.registerMobile(wechatUser,request.getMobile_number(),null);
                return successResult();
            } else {
                return new BaseResponse(400, "请填写手机验证码");
            }
        }
        // 验证完成后绑定微信 第二次请求
        if(mobileUser != null  && wechatUser == null &&  !StringUtils.isEmpty(request.getOpenid()) && !StringUtils.isEmpty(request.getAccess_token()) ){
            if(StringUtils.isEmpty(request.getInvite_code())){
                return new BaseResponse(400,"邀请码不能为空");
            }
            BaseResult<User> userBaseResult = userService.registerWechat(
                    mobileUser,
                    request.getOpenid(),
                    request.getAccess_token(),
                    request.getInvite_code(),
                    request.getNick_name(),
                    request.getHead_image(),
                    request.getUnionid());
            if(userBaseResult.hasError()){
                return onResult(userBaseResult);
            }
            return successResult(userBaseResult.getResult());
        }

        // 首先绑定微信 第一次请求
        if(mobileUser == null && wechatUser == null  && StringUtils.isEmpty(request.getMobile_number()) && !StringUtils.isEmpty(request.getOpenid()) && !StringUtils.isEmpty(request.getAccess_token()) ){
            userService.registerWechat(
                    mobileUser,
                    request.getOpenid(),
                    request.getAccess_token(),
                    null,
                    request.getNick_name(),
                    request.getHead_image(),
                    request.getUnionid()
            );
            return successResult();
        }
        // 绑定手机号码 第二次请求
        if(wechatUser != null && !StringUtils.isEmpty(request.getMobile_number()) ){
            if (!StringUtils.isEmpty(request.getVerification_code())) {
                if(StringUtils.isEmpty(request.getInvite_code())){
                    return new BaseResponse(400,"邀请码不能为空");
                }
                BaseResult baseResult = smsService.checkCode(request.getMobile_number(), request.getVerification_code(),SmsModelCode.LOGIN, false);
                if (baseResult.hasError()) {
                    return onResult(baseResult);
                }
                BaseResult<User> userBaseResult = userService.registerMobile(wechatUser, request.getMobile_number(), request.getInvite_code());
                    if(userBaseResult.hasError()){
                    return onResult(userBaseResult);
                }
                return successResult(userBaseResult.getResult());
            } else {
                return new BaseResponse(400, "请填写手机验证码");
            }
        }
        return new BaseResponse(400,"缺少请求参数");
    }


    @RequestMapping(value = "/mobile",method = RequestMethod.GET)
    public BaseResponse getMobile(String mobile,String code){
        if(StringUtils.isEmpty(mobile)){
           return setErrorResult(400,"缺少手机号码");
        }
        if(StringUtils.isEmpty(code)){
            return setErrorResult(400,"缺少手机验证码");
        }
        BaseResult baseResult = smsService.checkCode(mobile, code, SmsModelCode.LOGIN, true);
        if(baseResult.hasError()){
            return onResult(baseResult);
        }
        User mobileUser = userService.findByMobile(mobile);
        if(mobileUser == null){
            return setErrorResult(400,"手机号不存在");
        }
        return successResult(mobileUser);
    }


    @RequestMapping(value = "/wechat",method = RequestMethod.GET)
    public BaseResponse getWechat(String openid){
        if(StringUtils.isEmpty(openid)){
            return setErrorResult(400,"缺少微信openid");
        }
        User user = userService.findByOpenId(openid);
        return successResult(user);
    }

    @RequestMapping(value = "/captcha",method = RequestMethod.GET)
    public void getCaptcha(HttpServletRequest request, String mobile, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String s = RandomUtils.randomNumber(4);
        session.setAttribute(SESSION_CAPTCHA_ID,s);
        System.out.println(s + " ......2");
        byte[] image = CaptchafcUtil.getImage(s);
        response.setContentType("image/png");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(image);
        outputStream.flush();
        outputStream.close();
    }
}
