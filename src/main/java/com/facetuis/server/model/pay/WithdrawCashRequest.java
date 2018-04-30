package com.facetuis.server.model.pay;

import com.facetuis.server.model.basic.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

/**
 * 体现请求
 */
@Entity
@Table(name = "t_withdraw_cash")
public class WithdrawCashRequest extends BaseEntity {

    private String userId;
    private double amount;
    @JsonIgnore
    @Enumerated(EnumType.ORDINAL)
    private CashStatus status;
    private Date withdrawTime;// 提现时间


    public Date getWithdrawTime() {
        return withdrawTime;
    }

    public void setWithdrawTime(Date withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public CashStatus getStatus() {
        return status;
    }

    public void setStatus(CashStatus status) {
        this.status = status;
    }
}
