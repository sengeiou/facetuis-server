package com.facetuis.server.service.pinduoduo;

import com.facetuis.server.dao.user.UserCommisionRepository;
import com.facetuis.server.dao.user.UserRepository;
import com.facetuis.server.model.commision.CommisionSettings;
import com.facetuis.server.model.order.OrderCommision;
import com.facetuis.server.model.order.OrderStatus;
import com.facetuis.server.model.user.User;
import com.facetuis.server.model.user.UserCommision;
import com.facetuis.server.utils.RandomUtils;
import com.facetuis.server.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * 用户佣金计算
 */
@Service
public class UserCommisionService {

    private static final Logger logger  = Logger.getLogger(UserCommisionService.class.getName());

    @Autowired
    private UserCommisionRepository userCommisionRepository;
    @Autowired
    private UserRepository userRepository;


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
        double cash = userCommision.getUpdateCash() + price;
        userCommision.setUpdateCash(cash);
        userCommisionRepository.save(userCommision);
    }


    /**
     * 计算邀请奖励
     * 奖励上级用户
     * @param user
     */
    public void computInviting(final User user) {
        Date startTime = TimeUtils.stringToDate("2018-05-01 00:00:00");
        Date endTime = TimeUtils.stringToDateTime("2018-05-31 00:00:00");
        int randomCashMix = 1;
        int randomCashMax = 50;
        int newPeoples = 0; // 拉人多少人后才能获得奖励
        int rewardNum = 10;// 最多奖励人数
        //判断是否在当前时间段内
        int startTimeResult = TimeUtils.compare(startTime);
        int endTimeResult = TimeUtils.compare(endTime);
        if(startTimeResult >= 0 && endTimeResult <= 0) {
            // 上级已经邀请多少人
            String levelUserId = user.getLevelUserId();// 上级用户
            List<User> byLevelUserId = userRepository.findByLevelUserId(levelUserId);
            int invitingPeopleNum = byLevelUserId.size();// 上级邀请人数
            // 上级已经奖励人数
            UserCommision levelUserCommision = userCommisionRepository.findByUserId(levelUserId);
            if (levelUserCommision == null) {
                levelUserCommision = new UserCommision();
                levelUserCommision.setUuid(UUID.randomUUID().toString());
            }
            int invitingPeople = levelUserCommision.getInvitingPeople();

            // 拉新人数 ＞ 设定起始人数  && 已经奖励人数 ≤ 设定最多人数
            if (invitingPeopleNum > newPeoples && invitingPeople <= rewardNum) {
                //随机偶数奖励
                String rate = RandomUtils.rate(0, 10);
                Integer number = Integer.parseInt(rate);
                if (number % 2 == 0) {
                    //开启奖励
                    // 最终奖励金额
                    Double cash = Double.parseDouble(RandomUtils.rate(randomCashMix, randomCashMax));
                    double result = levelUserCommision.getInvitingCash() + cash;
                    levelUserCommision.setInvitingCash(result);
                    levelUserCommision.setInvitingPeople(levelUserCommision.getInvitingPeople() + 1);
                    userCommisionRepository.save(levelUserCommision);
                }else{
                    logger.info("随机结果::不奖励");
                }
            }
        }
    }
}
