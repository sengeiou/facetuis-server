package com.facetuis.server.app.web;

import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import com.facetuis.server.model.order.Order;
import com.facetuis.server.model.user.User;
import com.facetuis.server.service.pinduoduo.OrderService;
import com.facetuis.server.utils.NeedLogin;
import com.facetuis.server.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/1.0/order")
public class OrderController extends FacetuisController {


    @Autowired
    private OrderService orderService;

    @RequestMapping("/team/{orderStatus}")
    @NeedLogin(needLogin = true)
    public BaseResponse getTeamOrder(@PathVariable int orderStatus , String n, String m){
        User user = getUser();

        return successResult();
    }

    @RequestMapping("/team/orders/count")
    public BaseResponse getTeamOrdersCount(){
        PageRequest pageable = PageRequest.of(0,10);
        User user = getUser();
        String todayTime = TimeUtils.date2String(new Date());
        String yesterdayTime = TimeUtils.date2String(TimeUtils.getDateBefore(new Date(),1));
        // 当天订单
        Page<Order> todayOrders = orderService.findByDate(todayTime + " 00:00:00", todayTime + " 23:59:59", user.getUuid(), pageable);
        // 所有订单
        Page<Order> teamOrders = orderService.findTeamOrders(user.getUuid(), pageable);
        // 昨天订单
        Page<Order> yesterdayOrders = orderService.findByDate(yesterdayTime + " 00:00:00", yesterdayTime + " 23:59:59", user.getUuid(), pageable);
        //本月订单
        //String monthFirst = TimeUtils.
        //上月订单
        return successResult();
    }
}
