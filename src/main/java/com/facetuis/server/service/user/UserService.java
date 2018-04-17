package com.facetuis.server.service.user;

import com.facetuis.server.dao.user.UserRepository;
import com.facetuis.server.model.user.User;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.pinduoduo.PinDuoDuoService;
import com.facetuis.server.service.user.utils.UserUtils;
import com.facetuis.server.utils.RandomUtils;
import com.facetuis.server.utils.SysFinalValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PinDuoDuoService pinDuoDuoService;
    @Autowired
    private UserRelationService userRelationService;
    @Value("${sys.invite.code}")
    private String sysInviteCode;


    public User findRecommander(String userId){
        User user = userRepository.findById(userId).get();
        if(user.getLevelUserId().equals(SysFinalValue.SYS_USER_ID)){
            return null;
        }else{
            return userRepository.findById(user.getLevelUserId()).get();
        }
    }


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
     * 注册微信用户
     * @param user
     * @param openid
     * @param accessToken
     * @param inviteCode
     * @return
     */
    public BaseResult<User> registerWechat(User user, String openid, String accessToken, String inviteCode,String nickName,String headImg){
        BaseResult<User> userReslt = getUser(user,inviteCode);
        if(userReslt.hasError()){
            return userReslt;
        }
        user = userReslt.getResult();
        user.setOpenid(openid);
        user.setAccessToken(accessToken);
        user.setNickName(nickName);
        user.setHeadImg(headImg);
        createUser(user);
        return new BaseResult<>(user);
    }

    public void createUser(User user){
        userRepository.save(user);
        if(user.isEnable()){
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

    private String getLevelTxt(int level) {
        switch (level) {
            case 0:
                return userLevelNo1Txt;
            case 1:
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




}
