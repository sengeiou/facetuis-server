package com.facetuis.server.service.pinduoduo.scheduled;


import com.facetuis.server.model.order.Order;
import com.facetuis.server.service.pinduoduo.OrderService;
import com.facetuis.server.service.pinduoduo.response.OrderDetail;
import com.facetuis.server.service.pinduoduo.response.OrderListResponse;
import com.facetuis.server.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Component
public class OrderScheduled {

    private static final Logger logger = Logger.getLogger(OrderScheduled.class.getName());

    @Autowired
    private OrderService orderService;

    /**
     * 5 秒
     * 同步当天订单
     */
    @Scheduled(fixedRate=1000 * 5)
    public void syncToday() {
        String startTime = TimeUtils.date2String(new Date());
        String endTime = TimeUtils.date2String(new Date());
        syncOrders(startTime, endTime);
    }

    /**
     * 1 个小时
     * 同步5天订单
     */
    @Scheduled(fixedRate=1000 * 60 * 60 )
    public void sync5Day(){
        String startTime = TimeUtils.date2String(TimeUtils.getDateBefore(new Date(),5));
        String endTime = TimeUtils.date2String(TimeUtils.getDateBefore(new Date(),1));
        syncOrders(startTime, endTime);
    }
    /**
     *  2个小时
     * 同步10天订单
     */
    @Scheduled(fixedRate = 1000 * 60 * 60 * 2 )
    public void sync10Day(){
        String startTime = TimeUtils.date2String(TimeUtils.getDateBefore(new Date(),15));
        String endTime = TimeUtils.date2String(TimeUtils.getDateBefore(new Date(),6));
        syncOrders(startTime, endTime);
    }
    /**
     * 3 个小时
     * 同步20天订单
     */
    @Scheduled(fixedRate=1000 * 60 * 60 * 3)
    public void sync20Day(){
        String startTime = TimeUtils.date2String(TimeUtils.getDateBefore(new Date(),35));
        String endTime = TimeUtils.date2String(TimeUtils.getDateBefore(new Date(),14));
        syncOrders(startTime, endTime);
    }
    /**
     * 同步25天订单
     */
    @Scheduled(fixedRate=1000 * 60 * 60 * 4)
    public void sync25Day(){
        String startTime = TimeUtils.date2String(TimeUtils.getDateBefore(new Date(),60));
        String endTime = TimeUtils.date2String(TimeUtils.getDateBefore(new Date(),34));

        syncOrders(startTime, endTime);
    }


    private void syncOrders(String startTime, String endTime) {
        int page = 1;
        do {
            OrderListResponse response = orderService.getOrder(startTime, endTime, "", page);
            if(response == null || response.getOrder_list_get_response() == null){
                return;
            }
            if(response.getOrder_list_get_response().getTotal_count() == 0){
                return;
            }
            List<OrderDetail> order_list = response.getOrder_list_get_response().getOrder_list();
            System.out.println("同步订单：" + startTime + " | " + endTime + " | 本次同步：" + order_list.size());
            if( order_list != null && order_list.size() != 0){
                page = page + 1;
                orderService.updateOrder(response);
            }else{
                page = 0;
            }
        }while (page != 0);
    }

}
