package com.facetuis.server.service.pinduoduo.commision;

import com.facetuis.server.model.order.OrderCommision;
import com.facetuis.server.service.pinduoduo.response.OrderDetail;
import org.springframework.stereotype.Component;

@Component
public class CommisionSubStrategy implements CommisionStrategy {

    @Override
    public OrderCommision doCompute(final OrderDetail orderDetail) {
        return null;
    }
}
