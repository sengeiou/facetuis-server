package com.facetuis.server.service.pinduoduo;

import com.alibaba.fastjson.JSONObject;
import com.facetuis.server.dao.order.OrderRepository;
import com.facetuis.server.model.order.Order;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.pinduoduo.response.OrderDetail;
import com.facetuis.server.service.pinduoduo.response.OrderListResponse;
import com.facetuis.server.service.pinduoduo.utils.PRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class OrderService {

    @Autowired
    private PRequestUtils pRequestUtils;

    private String API_ORDER_RANGE = "pdd.ddk.order.list.range.get";
    @Autowired
    private OrderRepository orderRepository;

    /**
     * 查询订单
     * @param startTime
     * @param endTime
     * @param pid
     * @param page
     * @return
     */
    public OrderListResponse getOrder(String startTime,String endTime,String pid,int page) {
        SortedMap<String, String> map = new TreeMap<>();
        map.put("start_time", startTime);
        map.put("end_time", endTime);
        map.put("p_id", pid);
        map.put("page", page + "");
        map.put("page_size", "50");
        BaseResult<String> send = pRequestUtils.send(API_ORDER_RANGE, map);
        OrderListResponse response = JSONObject.parseObject(send.getResult(), OrderListResponse.class);
        return response;
    }

    public void updateOrder(OrderListResponse orderList){
        List<OrderDetail> order_list = orderList.getOrder_list();
        if(order_list != null && order_list.size() > 0){
            List<String> orderSnList = new ArrayList<>();
            for(OrderDetail order : order_list){
                orderSnList.add(order.getOrder_sn());
            }
            List<Order> orders = orderRepository.findByOrder_snIn(orderSnList);
            for(OrderDetail order : order_list){
                //for()
            }
        }
    }


}
