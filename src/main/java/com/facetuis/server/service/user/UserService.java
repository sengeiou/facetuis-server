package com.facetuis.server.service.user;

import com.facetuis.server.dao.user.UserRepository;
import com.facetuis.server.model.pay.Payment;
import com.facetuis.server.model.user.User;
import com.facetuis.server.model.user.UserLevel;
import com.facetuis.server.model.user.UserRelation;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.basic.BasicService;
import com.facetuis.server.service.payment.PaymentService;
import com.facetuis.server.service.pinduoduo.PinDuoDuoService;
import com.facetuis.server.service.user.utils.UserUtils;
import com.facetuis.server.service.wechat.WechatService;
import com.facetuis.server.service.wechat.response.MpGetUserInfoResponse;
import com.facetuis.server.utils.RandomUtils;
import com.facetuis.server.utils.SysFinalValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class UserService extends BasicService {

    private static Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PinDuoDuoService pinDuoDuoService;
    @Autowired
    private UserRelationService userRelationService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private WechatService wechatService;


    @Value("${sys.invite.code}")
    private String sysInviteCode;
    @Value("${sys.recommend.url}")
    private String recommendUrl;


    public User findRecommander(String userId){
        User user = userRepository.findById(userId).get();
        if(user.getLevelUserId().equals(SysFinalValue.SYS_USER_ID)){
            return null;
        }else{
            return userRepository.findById(user.getLevelUserId()).get();
        }
    }


    public User findByUnionid(String unionid){
        return userRepository.findByUnionId(unionid);
    }

    public User findByMobile(String mobile){
        User user = userRepository.findByMobileNumber(mobile);
        user.setRecommendUrl(String.format(recommendUrl,user.getRecommandCode()));
        return user;
    }

    public User findByOpenId(String openid){
        User user = userRepository.findByOpenid(openid);
        user.setRecommendUrl(String.format(recommendUrl,user.getRecommandCode()));
        return user;
    }

    public User login(String userId){
        User user = userRepository.findById(userId).get();
        user.setLoginTime(new Date());
        user.setToken(RandomUtils.random(64));
        user.setRecommendUrl(String.format(recommendUrl,user.getRecommandCode()));
        return userRepository.save(user);
    }


    /**
     * 注册手机用户
     * @param user
     * @param mobile
     * @param inviteCode
     * @return
     */
    public  BaseResult<User> registerMobile(User user, String mobile,String inviteCode) {
        BaseResult<User> userReslt = getUser(user,inviteCode);
        if(userReslt.hasError()){
            return userReslt;
        }
        user = userReslt.getResult();
        user.setMobileNumber(mobile);
        createUser(user);
        return new BaseResult<>(user);
    }

    /**
     * 通过网页注册微信用户
     * @param user
     * @param openid
     * @param accessToken
     * @return
     */
    public BaseResult registerWechatByWeb(User user,String openid,String accessToken){
        // 根据公众号信息获取用户信息
        BaseResult<MpGetUserInfoResponse> userInfo = wechatService.getUserInfo(openid, accessToken);
        if(userInfo.hasError()){
           return userInfo;
        }
        MpGetUserInfoResponse userInfoResponse = userInfo.getResult();
        // 注册微信用户
        registerWechat(user,
                userInfoResponse.getOpenid(),
                accessToken,
                user.getInviteCode(),
                userInfoResponse.getNickname(),
                userInfoResponse.getHeadimgurl(),
                userInfoResponse.getUnionid());

        return new BaseResult();
    }

    /**
     * 注册微信用户
     * @param user
     * @param openid
     * @param accessToken
     * @param inviteCode
     * @return
     */
    public BaseResult<User> registerWechat(User user, String openid, String accessToken, String inviteCode,String nickName,String headImg,String unionid){
        userRepository.findByOpenid(openid);
        BaseResult<User> userReslt = getUser(user,inviteCode);
        if(userReslt.hasError()){
            return userReslt;
        }
        user = userReslt.getResult();
        user.setOpenid(openid);
        user.setAccessToken(accessToken);
        user.setNickName(nickName);
        user.setHeadImg(headImg);
        user.setUnionId(unionid);
        createUser(user);
        return new BaseResult<>(user);
    }

    public void createUser(User user){
        userRepository.save(user);
        if(user.getEnable()){
            // 保存用户关系
            userRelationService.initUserRelation(user);
        }
    }

    public String  getRecommandCode(){
        String code = UserUtils.generateRecommandCode();
        User user = userRepository.findByRecommandCode(code);
        if(user == null){
            return code;
        }
        return  getRecommandCode();
    }


    private BaseResult<User> getUser(User user,String  inviteCode) {
        if(user == null){
            user = new User();
            user.setUuid(UUID.randomUUID().toString());
            user.setEnable(false);
        }else{
            if(inviteCode.length() < 5){
                return new BaseResult<>(600,"邀请码格式错误");
            }
            // 邀请码
            String inviteUserId = "";
            if(inviteCode.equals(sysInviteCode)){
                inviteUserId = SysFinalValue.SYS_USER_ID;
            }else{
                User tempUser = userRepository.findByRecommandCodeLike( "%," + inviteCode + ",%");
                if(tempUser == null){
                    return new BaseResult<>(600,"邀请码无效");
                }
                inviteUserId = tempUser.getUuid();
            }
            // 设置推广位
            String pid = pinDuoDuoService.createPid();
            if(StringUtils.isEmpty(pid)){
                return new BaseResult<>(600,"推广位创建失败");
            }
            user.setPid(pid);
            user.setLevelUserId(inviteUserId);
            user.setEnable(true);
            user.setRecommandCode("," + getRecommandCode()+ ",");
            user.setToken(RandomUtils.random(64));
            user.setLoginTime(new Date());
            user.setLevelTxt(getLevelTxt(user.getLevel()));
            user.setInviteCode( inviteCode );
        }
        return new BaseResult<>(user);
    }


    @Value("${user.level.no1.txt}")
    private String userLevelNo1Txt;
    @Value("${user.level.no2.txt}")
    private String userLevelNo2Txt;

    private String getLevelTxt(UserLevel level) {
        switch (level) {
            case LEVEL1:
                return userLevelNo1Txt;
            case LEVEL2:
                return userLevelNo2Txt;
        }
        return "";
    }


    public User getUserByToken(String token) {
        return userRepository.findByToken(token);
    }

    public List<User> findByIds(List<String> userIds){
        return userRepository.findAllById(userIds);
    }

    public List<User> findByNickNameOrMobile(String nickName,String mobile){
        List<User> users = new ArrayList<>();
        if(!StringUtils.isEmpty(nickName)){
            List<User> byNickName = userRepository.findByNickName(nickName);
            users.addAll(byNickName);
        }else if(!StringUtils.isEmpty(mobile)){
            User byMobileNumber = userRepository.findByMobileNumber(mobile);
            users.add(byMobileNumber);
        }
        return users;
    }

    public List<User> findByInviteCode(String inviteCode){
       return userRepository.findByInviteCode(inviteCode);
    }

    /**
     * 用户升级
     * @return
     */
    public BaseResult upload(String userId,String tradeNo,String productId){
        // 查询用户交易状态
        Payment payment = paymentService.findByTradeNo(tradeNo);
        if(payment == null){
            return new BaseResult(600,"支付信息不存在");
        }
        if(!payment.getProductId().equals(productId)){
            return new BaseResult(600,"支付产品不存在");
        }
        if(!userId.equals(payment.getUserId())){
            return new BaseResult(600,"支付用户不匹配");
        }
        User user = userRepository.findById(userId).get();
        if(user != null){
            UserRelation relation = userRelationService.getRelation(user.getUuid());
            Integer user1Total = relation.getUser1Total();
            if(user1Total < 20){
                return new BaseResult(600,"升级要求未达到：直属会员 ≥ 20人");
            }
            if(relation.getUser2Total() + relation.getUser3Total() < 40){
                return new BaseResult(600,"升级要求未达到：直属会员下级 ≤ 40人");
            }

            if(user.getLevel().equals(UserLevel.LEVEL1)){
                user.setLevel(UserLevel.LEVEL2);
                user.setLevelTxt(getLevelTxt(user.getLevel()));
                userRepository.save(user);
                // 将用户添加到上级用户的高级用户列表
                if(!SysFinalValue.SYS_USER_ID.equals(user.getLevelUserId())){
                    userRelationService.addHigher(user.getUuid(),user.getLevelUserId());
                }
            }else{
                logger.info("用户" + userId + "已是最高级别，不能在升级" );
            }
        }
        return new BaseResult();
    }



}
