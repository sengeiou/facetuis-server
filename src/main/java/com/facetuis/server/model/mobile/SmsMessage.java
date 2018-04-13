package com.facetuis.server.model.mobile;

import com.facetuis.server.model.basic.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_sms_message")
public class SmsMessage extends BaseEntity {

    // 手机号码
    private String mobileNumber;
    // 模块代码
    @Enumerated(EnumType.ORDINAL)
    private SmsModelCode modelCode;
    // 过期时间 （秒）
    private int expiresIn = 60;
    // 验证码
    private String code;
    // 是否已经验证
    private boolean isRead = false;
    // 短信内容
    private String  message;
    // 短信平台响应code
    private int smsCode;
    private String smsid;

    public int getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(int smsCode) {
        this.smsCode = smsCode;
    }

    public String getSmsid() {
        return smsid;
    }

    public void setSmsid(String smsid) {
        this.smsid = smsid;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public SmsModelCode getModelCode() {
        return modelCode;
    }

    public void setModelCode(SmsModelCode modelCode) {
        this.modelCode = modelCode;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
