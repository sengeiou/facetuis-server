package com.facetuis.server.service.user;

import com.facetuis.server.dao.user.UserRepository;
import com.facetuis.server.model.user.User;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.user.utils.UserUtils;
import com.facetuis.server.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService {




    @Autowired
    private UserRepository userRepository;

    public User findByMobile(String mobile){
        return userRepository.findByMobileNumber(mobile);
    }

    public User findByOpenId(String openid){
        return userRepository.findByOpenid(openid);
    }

    public User login(String userId){
        User user = userRepository.findById(userId).get();
        user.setLoginTime(new Date());
        user.setToken(RandomUtils.random(64));
        return userRepository.save(user);
    }


    public  BaseResult<User> registerMobile(User user, String mobile,String inviteCode) {
        BaseResult<User> userReslt = getUser(user,inviteCode);
        if(userReslt.hasError()){
            return userReslt;
        }
        user.setMobileNumber(mobile);
        userRepository.save(user);
        return new BaseResult<>(user);
    }
    public BaseResult<User> registerWechat(User user, String openid, String accessToken, String inviteCode){
        BaseResult<User> userReslt = getUser(user,inviteCode);
        if(userReslt.hasError()){
            return userReslt;
        }
        user = userReslt.getResult();
        user.setOpenid(openid);
        user.setAccessToken(accessToken);
        userRepository.save(user);
        return new BaseResult<>(user);
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
        }else{
            // 判断邀请码是否存在
            User tempUser = userRepository.findByInviteCode(inviteCode);
            if(tempUser == null){
                return new BaseResult<>(600,"邀请码无效");
            }
            user.setLevelUserId(tempUser.getUuid());
            user.setEnable(true);
            user.setRecommandCode(getRecommandCode());
            user.setToken(RandomUtils.random(64));
            user.setLoginTime(new Date());
            user.setLevelTxt(getLevelTxt(user.getLevel()));
            user.setInviteCode(inviteCode);
        }
        return new BaseResult<>(user);
    }


    @Value("${user.level.no1.txt}")
    private String userLevelNo1Txt;
    @Value("${user.level.no2.txt}")
    private String userLevelNo2Txt;

    private String getLevelTxt(int level) {
        switch (level) {
            case 0:
                return userLevelNo1Txt;
            case 1:
                return userLevelNo2Txt;
        }
        return "";
    }



}
