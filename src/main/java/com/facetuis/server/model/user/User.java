package com.facetuis.server.model.user;

import com.facetuis.server.model.basic.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "t_users")
@Component
public class User extends BaseEntity {


    private String nickName;
    private String openid;
    @JsonProperty("mobile_number")
    private String mobileNumber;
    @JsonProperty("invite_code")
    private String inviteCode; // 邀请码
    private String token;
    private Date loginTime;
    @JsonProperty("access_token")
    private String accessToken;//微信票据
    private boolean enable = false;
    private int level = 0;
    @JsonProperty("level_txt")
    private String levelTxt;
    private Long loginExpiresIn = 1000 * 60 * 60 * 24 * 30l ;
    private String recommandCode;// 推荐码 属于自己的邀请码 拉下线使用
    private String levelUserId;// 上级ID

    public String getLevelUserId() {
        return levelUserId;
    }

    public void setLevelUserId(String levelUserId) {
        this.levelUserId = levelUserId;
    }

    public Long getLoginExpiresIn() {
        return loginExpiresIn;
    }

    public void setLoginExpiresIn(Long loginExpiresIn) {
        this.loginExpiresIn = loginExpiresIn;
    }

    public String getRecommandCode() {
        return recommandCode;
    }

    public void setRecommandCode(String recommandCode) {
        this.recommandCode = recommandCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public String getLevelTxt() {
        return levelTxt;
    }

    public void setLevelTxt(String levelTxt) {
        this.levelTxt = levelTxt;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
