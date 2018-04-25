package com.facetuis.server.model.pay;

import com.facetuis.server.model.basic.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "t_withdraw_cash")
public class WithdrawCashRequest extends BaseEntity {

    private String userId;
    private String amount;
    @Enumerated(EnumType.ORDINAL)
    private CashStatus status;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public CashStatus getStatus() {
        return status;
    }

    public void setStatus(CashStatus status) {
        this.status = status;
    }
}
