package com.facetuis.server.dao.user;

import com.facetuis.server.model.mobile.SmsMessage;
import com.facetuis.server.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {

    public User findByMobileNumber(String mobileNumber);

    public User findByOpenid(String openId);

    User findByRecommandCode(String recommandCode);

    User findByInviteCode(String inviteCode);
}
