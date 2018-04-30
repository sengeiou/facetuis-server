package com.facetuis.server.service.pinduoduo;

import com.facetuis.server.dao.user.UserCommisionRepository;
import com.facetuis.server.model.commision.CommisionSettings;
import com.facetuis.server.model.order.OrderCommision;
import com.facetuis.server.model.order.OrderStatus;
import com.facetuis.server.model.user.UserCommision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 用户佣金计算
 */
@Service
public class UserCommisionService {

    @Autowired
    private UserCommisionRepository userCommisionRepository;


    /**
     * 计算个人佣金
     * 订单状态为已结算的订单 && 订单佣金未添加到个人账户中（CommisionStrategy.isFinish == false） ==> 计算个人账户佣金情况：可提现
     * @param orderStatus
     * @param orderCommision
     */
    public void computUserCommision(OrderStatus orderStatus, OrderCommision orderCommision) {
        if(!orderCommision.getWaitFinish() || !orderCommision.getFinish()) {
            UserCommision userCommison = userCommisionRepository.findByUserId(orderCommision.getUserId());//购买用户
            if (userCommison == null) {
                userCommison = new UserCommision();
                userCommison.setUuid(UUID.randomUUID().toString());
            }
            UserCommision user1Commison = userCommisionRepository.findByUserId(orderCommision.getUser1Id());//购买用户上级
            if (user1Commison == null) {
                user1Commison = new UserCommision();
                user1Commison.setUuid(UUID.randomUUID().toString());
            }
            UserCommision user2Commison = userCommisionRepository.findByUserId(orderCommision.getUser2Id());//购买用户上级上级
            if (user2Commison == null) {
                user2Commison = new UserCommision();
                user2Commison.setUuid(UUID.randomUUID().toString());
            }
            UserCommision user3Commison = userCommisionRepository.findByUserId(orderCommision.getUser3Id());//购买用户上级上级
            if (user3Commison == null) {
                user3Commison = new UserCommision();
                user3Commison.setUuid(UUID.randomUUID().toString());
            }
            // 佣金已结算 && 已完成佣金结算
            if (orderStatus == OrderStatus.SETTLEMENT && orderCommision.getFinish()) {
                // 计算订单可提现金额 = 用户分拥金额
                // 计算
                Double userCommisionAmount = userCommison.getOrderCash() + orderCommision.getUserCommision();// 购买用户的佣金
                userCommison.setOrderCash(userCommisionAmount);
                userCommisionRepository.save(userCommison);

                Double user1CommisionAmount = user1Commison.getOrderCash() + orderCommision.getUser1Commision();// 上级用户的佣金
                user1Commison.setOrderCash(user1CommisionAmount);
                userCommisionRepository.save(user1Commison);

                Double user2CommisionAmount = user2Commison.getOrderCash() + orderCommision.getUser2Commision();// 上级的上级佣金
                user2Commison.setOrderCash(user2CommisionAmount);
                userCommisionRepository.save(user2Commison);

                Double user3CommisionAmount = user3Commison.getOrderCash() + orderCommision.getUser3Commision();// 上级的上级佣金
                user3Commison.setOrderCash(user3CommisionAmount);
                userCommisionRepository.save(user3Commison);
                // 结算金额计算完成
                orderCommision.setFinish(true);
            } else if (!orderCommision.getWaitFinish()) {
                // 佣金未结算 =》 佣金待结算 = 支付状态为未结算的订单 + 上次未结算的佣金金额 + 要结算的佣金金额
                Double userCommisionAmount = userCommison.getWaitSettlement() + orderCommision.getUserCommision();// 购买用户的佣金
                userCommison.setWaitSettlement(userCommisionAmount);
                userCommisionRepository.save(userCommison);

                Double user1CommisionAmount = user1Commison.getWaitSettlement() + orderCommision.getUser1Commision();// 上级用户的佣金
                user1Commison.setWaitSettlement(user1CommisionAmount);
                userCommisionRepository.save(user1Commison);

                Double user2CommisionAmount = user2Commison.getWaitSettlement() + orderCommision.getUser2Commision();// 上级的上级佣金
                user2Commison.setWaitSettlement(user2CommisionAmount);
                userCommisionRepository.save(user2Commison);

                Double user3CommisionAmount = user3Commison.getWaitSettlement() + orderCommision.getUser3Commision();// 上级的上级佣金
                user3Commison.setWaitSettlement(user3CommisionAmount);
                userCommisionRepository.save(user3Commison);

                orderCommision.setWaitFinish(true);
            }
        }
    }


    /**
     * 邀请用户奖励
     * @param userId
     * @param price
     */
    public void addInvitingCash(String userId,Double price){
        UserCommision userCommision = userCommisionRepository.findByUserId(userId);
        if(userCommision == null){
            userCommision = new UserCommision();
            userCommision.setUuid(UUID.randomUUID().toString());
        }
        double cash = userCommision.getInvitingCash() + price;
        userCommision.setInvitingCash(cash);
        userCommisionRepository.save(userCommision);
    }
}
