package com.facetuis.server.app.web.request;

import javax.validation.constraints.NotEmpty;

public class WechatTokenRequest {

    @NotEmpty
    private String code;
    private String openid;
    private String userid;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
