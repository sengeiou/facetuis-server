package com.facetuis.server.service.pinduoduo;

import com.facetuis.server.dao.order.OrderCommisionRepository;
import com.facetuis.server.model.order.OrderCommision;
import com.facetuis.server.service.pinduoduo.commision.CommisionContext;
import com.facetuis.server.service.pinduoduo.commision.CommisionStrategy;
import com.facetuis.server.service.pinduoduo.response.OrderDetail;
import com.facetuis.server.service.pinduoduo.response.OrderListBody;
import com.facetuis.server.service.pinduoduo.response.OrderListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderCommisionService {

    @Autowired
    private OrderCommisionRepository orderCommisionRepository;

    @Autowired
    private CommisionContext commisionContext;

    /**
     * 订单佣金计算
     * @param response
     */
    public void compute(OrderListResponse response){
        if(response == null || response.getOrder_list_get_response() == null){
            return;
        }
        List<OrderDetail> order_list = response.getOrder_list_get_response().getOrder_list();
        for(OrderDetail orderDetail : order_list){
            CommisionStrategy strategy = commisionContext.getStrategy(orderDetail.getOrderStatus());
            if(strategy != null) {
                OrderCommision orderCommision = strategy.doCompute(orderDetail);
                if(orderCommision != null){
                    orderCommisionRepository.save(orderCommision);
                }
            }
        }
        return;
    }
}
