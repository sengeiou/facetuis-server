package com.facetuis.server.service.pinduoduo.commision;

import com.facetuis.server.dao.order.OrderCommisionRepository;
import com.facetuis.server.dao.user.UserRelationRepository;
import com.facetuis.server.dao.user.UserRepository;
import com.facetuis.server.model.order.OrderCommision;
import com.facetuis.server.model.user.User;
import com.facetuis.server.model.user.UserRelation;
import com.facetuis.server.service.pinduoduo.response.OrderDetail;
import com.facetuis.server.utils.SysFinalValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        // 如果计算完成
        if(!orderCommision.getCompute()){
            // 计算佣金
            // 购买人
            User user = userRepository.findByPid(orderDetail.getpId());
            if(SysFinalValue.SYS_USER_ID.equals(user.getLevelUserId()) ){
               // 购买人无上级
            }else {
                UserRelation userRelation = userRelationRepository.findByUserId(user.getUuid());
                // 购买人 3个上级
                String userLevel1Id = userRelation.getUserLevel1Id();
                String userLevel2Id = userRelation.getUserLevel2Id();
                String userLevel3Id = userRelation.getUserLevel3Id();
                userRepository.findById(userLevel1Id);
            }




        }
        return null;
    }
}
