package com.facetuis.server.service.pinduoduo.commision;

import com.facetuis.server.dao.order.OrderCommisionRepository;
import com.facetuis.server.dao.user.UserRelationRepository;
import com.facetuis.server.dao.user.UserRepository;
import com.facetuis.server.model.order.OrderCommision;
import com.facetuis.server.model.user.User;
import com.facetuis.server.model.user.UserRelation;
import com.facetuis.server.service.pinduoduo.response.OrderDetail;
import com.facetuis.server.utils.CommisionUtils;
import com.facetuis.server.utils.CommisionUser;
import com.facetuis.server.utils.SysFinalValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 佣金增加计算
 */
@Component
public class CommisionAddStrategy implements CommisionStrategy {

    @Autowired
    private OrderCommisionRepository orderCommisionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRelationRepository userRelationRepository;

    @Override
    public OrderCommision doCompute(final OrderDetail orderDetail) {
        OrderCommision orderCommision = orderCommisionRepository.findByOrderSn(orderDetail.getOrderSn());
        if(orderCommision == null){
            orderCommision = new OrderCommision();
            BeanUtils.copyProperties(orderDetail,orderCommision);
        }
        long promotionAmount = orderDetail.getPromotionAmount();
        // 如果计算完成
        if(!orderCommision.getCompute()){
            //orderDetail.get
            StringBuilder levleRelation = new  StringBuilder();
            // 计算佣金
            String userLevel1Id = "";
            String userLevel2Id = "";
            String userLevel3Id = "";
            // 购买人
            User user = userRepository.findByPid(orderDetail.getpId());
            if(SysFinalValue.SYS_USER_ID.equals(user.getLevelUserId()) ){
               // 购买人无上级
                levleRelation.append(user.getLevel().getLevel());
                levleRelation.append("0");
                levleRelation.append("0");
                levleRelation.append("0");
            }else {
                UserRelation userRelation = userRelationRepository.findByUserId(user.getUuid());
                // 购买人 3个上级
                userLevel1Id = userRelation.getUserLevel1Id();
                levleRelation.append(getLevel(userLevel1Id));
                userLevel2Id = userRelation.getUserLevel2Id();
                levleRelation.append(getLevel(userLevel2Id));
                userLevel3Id = userRelation.getUserLevel3Id();
                levleRelation.append(getLevel(userLevel3Id));
            }
            orderCommision.setUserId(user.getUuid());
            orderCommision.setUser1Id(userLevel1Id);
            orderCommision.setUser2Id(userLevel1Id);
            orderCommision.setUser3Id(userLevel1Id);
            CommisionUser rate = CommisionUtils.getRate(levleRelation.toString());
            orderCommision.setUserCommision(CommisionUtils.multiply(promotionAmount,rate.getUserRate(),0));
            orderCommision.setUser1Commision(CommisionUtils.multiply(promotionAmount,rate.getUser1Rate(),0));
            orderCommision.setUser2Commision(CommisionUtils.multiply(promotionAmount,rate.getUser2Rate(),0));
            orderCommision.setUser3Commision(CommisionUtils.multiply(promotionAmount,rate.getUser3Rate(),0));
        }
        return orderCommision;
    }


    public int getLevel(String userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            return optionalUser.get().getLevel().getLevel();
        }
        return 0;
    }


}
