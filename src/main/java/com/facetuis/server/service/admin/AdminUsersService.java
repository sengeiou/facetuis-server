package com.facetuis.server.service.admin;

import com.facetuis.server.dao.admin.AdminUserRepository;
import com.facetuis.server.model.admin.AdminUsers;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.basic.BasicService;
import com.facetuis.server.utils.MD5Utils;
import com.facetuis.server.utils.RandomUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminUsersService extends BasicService {
    @Autowired
    private AdminUserRepository adminUserRepository;


    //根据userName 查用户信息并返回用户信息
    public BaseResult<AdminUsers> login(String user, String pass)
    {
        if(StringUtils.isEmpty(user) || StringUtils.isEmpty(pass) )
        {
            return null;
        }
        AdminUsers   adminUsers= adminUserRepository.findByUserName(user);

        if(adminUsers == null){
            return new BaseResult<>(500,"用户名不存在！");
        }

        String p1= MD5Utils.MD5(pass);
        String p2=MD5Utils.MD5(p1);
        String p3=MD5Utils.MD5(p2);
        String p4= MD5Utils.MD5( p3+"wjymd");
        //String p3=MD
        if(!adminUsers.getPassword().equals(p4))
        {
            return new BaseResult<>(600,"用户名或密码错误！");

        }
        //生成Auto_token
        adminUsers.setAccessToken(RandomUtils.random(64));
        adminUserRepository.save(adminUsers);
        return  new BaseResult<>(adminUsers );
    }


    public AdminUsers findByAccessToken(String token){
        return adminUserRepository.findByAccessToken(token);
    }


    public static void main(String[] args) {
        String p1= MD5Utils.MD5("123456");
        String p2=MD5Utils.MD5(p1);
        String p3=MD5Utils.MD5(p2);
        String p4= MD5Utils.MD5( p3+"wjymd");
        System.out.println(p4);
    }
}
