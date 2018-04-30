package com.facetuis.server.model.user;

import com.facetuis.server.model.basic.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "t_user_commision")
public class UserCommision extends BaseEntity {

    private String userId;
    private Double orderCash = 0.0; // 订单可提现金额
    private Double waitSettlement = 0.0; // 等待结算金额
    private Double finishSettlement = 0.0;// 已完结结算金额
    @Enumerated(EnumType.STRING)
    private CashStatus cashStatus; // 提现状态
    private Double finishCash;// 已经提现金额
    private Double invitingCash = 0.0;// 邀请用户奖励

    public Double getInvitingCash() {
        return invitingCash;
    }

    public void setInvitingCash(Double invitingCash) {
        this.invitingCash = invitingCash;
    }

    public Double getFinishCash() {
        return finishCash;
    }

    public void setFinishCash(Double finishCash) {
        this.finishCash = finishCash;
    }

    public CashStatus getCashStatus() {
        return cashStatus;
    }

    public void setCashStatus(CashStatus cashStatus) {
        this.cashStatus = cashStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getOrderCash() {
        return orderCash;
    }

    public void setOrderCash(Double orderCash) {
        this.orderCash = orderCash;
    }

    public Double getWaitSettlement() {
        return waitSettlement;
    }

    public void setWaitSettlement(Double waitSettlement) {
        this.waitSettlement = waitSettlement;
    }

    public Double getFinishSettlement() {
        return finishSettlement;
    }

    public void setFinishSettlement(Double finishSettlement) {
        this.finishSettlement = finishSettlement;
    }
}
