package com.facetuis.server.service.pinduoduo.scheduled;


import com.facetuis.server.model.order.Order;
import com.facetuis.server.service.pinduoduo.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class OrderScheduled {

    private static final Logger logger = Logger.getLogger(OrderScheduled.class.getName());

    @Autowired
    private OrderService orderService;

    @Scheduled(fixedRate=1000 * 5)
    public void statusCheck() {
        orderService.getOrder("2018-04-13","2018-04-13","",1);
    }
}
