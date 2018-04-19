package com.facetuis.server.service.pinduoduo.commision;

import com.facetuis.server.dao.order.OrderCommisionRepository;
import com.facetuis.server.model.order.OrderCommision;
import com.facetuis.server.service.pinduoduo.response.OrderDetail;
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

        }
        return null;
    }
}
