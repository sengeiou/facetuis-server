package com.facetuis.server.service.pinduoduo.task;

import com.facetuis.server.service.pinduoduo.OrderService;
import com.facetuis.server.service.pinduoduo.response.OrderListResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class OrderTask {

    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private OrderService orderService;


    @Async("threadCommisionExecutor")
    public void doUpdateOrderTask(OrderListResponse response){
        orderService.updateOrder(response);
    }

    @Async("threadOrderUpdateExecutor")
    public void doComputeOrderTask(OrderListResponse response){

    }

}
