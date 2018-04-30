package com.facetuis.server.app.web;

import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import com.facetuis.server.app.web.request.RegisterRequest;
import com.facetuis.server.app.web.request.UpgradedRequest;
import com.facetuis.server.app.web.request.UserSettingsRequest;
import com.facetuis.server.app.web.response.UserRecommanderResponse;
import com.facetuis.server.model.commision.CommisionSettings;
import com.facetuis.server.model.product.Product;
import com.facetuis.server.model.user.User;
import com.facetuis.server.model.user.UserCommision;
import com.facetuis.server.model.user.UserLevel;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.pinduoduo.UserCommisionService;
import com.facetuis.server.service.user.UserService;
import com.facetuis.server.utils.NeedLogin;
import com.facetuis.server.utils.ProductUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/1.0/user")
public class UserController extends FacetuisController{

    @Autowired
    private UserService userService;
    @Value("${wechat.mp.code.url}")
    private String wechatMpCodeUrl;
    @Value("${wechat.app.id}")
    private String appid;
    @Value("${sys.server.host}")
    private String host;
    @Value("${sys.web.register.url}")
    private String webRegister;
    @Autowired
    private UserCommisionService userCommisionService;


    /**
     * 网页注册
     * @param registerRequest
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public BaseResponse register(@RequestBody RegisterRequest registerRequest,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return erroorResult(bindingResult);
        }
        User mobileUser = userService.findByMobile(registerRequest.getMobileNumber());
        if(mobileUser != null){
            return setErrorResult(600,"用户已存在");
        }
        if(!StringUtils.isEmpty(registerRequest.getOpenid())){
            User unionidUser = userService.findByOpenId(registerRequest.getOpenid());
            if(unionidUser != null){
                return setErrorResult(600,"用户已存在");
            }
        }
        BaseResult<User> userBaseResult = userService.registerMobile(mobileUser, registerRequest.getMobileNumber(), registerRequest.getInviteCode());
        if(userBaseResult.hasError()){
            return onResult(userBaseResult);
        }else{
            User user = userBaseResult.getResult();
            user.setInviteCode(registerRequest.getInviteCode());
            BaseResult baseResult = userService.registerWechatByWeb(user, registerRequest.getOpenid(), registerRequest.getAccessToken());
            if(baseResult.hasError()){
                return onResult(baseResult);
            }
        }
        return successResult();
    }

    @RequestMapping(value = "/setting",method = RequestMethod.POST)
    @NeedLogin(needLogin = true)
    public BaseResponse setting(@RequestBody UserSettingsRequest userSettingsRequest){
        User user = getUser();
        user.setSettingRQInGoodsImage(userSettingsRequest.getSettingRQInGoodsImage());
        userService.save(user);
        userSettingsRequest.getSettingRQInGoodsImage();
        return successResult();
    }


    /**
     * 推广链接
     * @return
     */
    @RequestMapping(value = "/enroll/{recommender}",method = RequestMethod.GET)
    public void enroll(@PathVariable String recommender, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ua = request.getHeader("user-agent");
        if (ua.indexOf("micromessenger") > 0) {
            String url = "http://" + host + "/facetuis/1.0/wechat/mp/userinfo/" + recommender;
            url = URLEncoder.encode(url,"UTF-8");
            String location = String.format(wechatMpCodeUrl, appid, url,recommender);
            // 跳转到微信
            response.sendRedirect(location);
            return;
        }
        response.sendRedirect(webRegister);
    }


    /**
     * 获取推荐自己的用户
     * @return
     */
    @RequestMapping(value = "/recommender",method = RequestMethod.GET)
    @NeedLogin(needLogin = true)
    public BaseResponse getCommander() {
        User user = getUser();
        UserRecommanderResponse response = new UserRecommanderResponse();
        User recommander = userService.findRecommander(user.getUuid());
        if(recommander == null){
            return successResult(response);
        }
        BeanUtils.copyProperties(recommander, response);
        return successResult(new UserRecommanderResponse());
    }

    /**
     * 用户升级
     * @return
     */
    @RequestMapping(value = "/upgraded",method = RequestMethod.POST)
    @NeedLogin(needLogin = true)
    public BaseResponse upgraded(@RequestBody UpgradedRequest request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return setErrorResult(bindingResult,400,"请求参数错误");
        }
        User user = getUser();
        if(user.getLevel().equals(UserLevel.LEVEL2)){
            return setErrorResult(600,"已经是最高级");
        }
        BaseResult upload = userService.upload(user.getUuid(), request.getOutTradeNo(), request.getProductId());
        if(!upload.hasError()){
            CommisionSettings commisionSettings = new CommisionSettings();
            Product product = ProductUtils.getProduct(request.getProductId());
            Double cash = 0.0;
            if(product.getValues() == 30){
                cash = commisionSettings.getMonthAdd();
            }else{
                cash = commisionSettings.getYearAdd();
            }
            // 发送奖励
            userCommisionService.addInvitingCash(user.getUuid(),cash);
        }
        return onResult(upload);
    }




}
