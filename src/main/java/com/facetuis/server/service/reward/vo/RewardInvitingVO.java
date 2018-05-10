package com.facetuis.server.service.reward.vo;

import java.util.Date;

public class RewardInvitingVO {

    private String invitingStart;
    private String invitingEnd;
    private long totalAmount;
    private long todayAmount;
    private long yesterdayAmount;
    private long monthAmount;

    public String getInvitingStart() {
        return invitingStart;
    }

    public void setInvitingStart(String invitingStart) {
        this.invitingStart = invitingStart;
    }

    public String getInvitingEnd() {
        return invitingEnd;
    }

    public void setInvitingEnd(String invitingEnd) {
        this.invitingEnd = invitingEnd;
    }

    public long getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(long monthAmount) {
        this.monthAmount = monthAmount;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getTodayAmount() {
        return todayAmount;
    }

    public void setTodayAmount(long todayAmount) {
        this.todayAmount = todayAmount;
    }

    public long getYesterdayAmount() {
        return yesterdayAmount;
    }

    public void setYesterdayAmount(long yesterdayAmount) {
        this.yesterdayAmount = yesterdayAmount;
    }
}
