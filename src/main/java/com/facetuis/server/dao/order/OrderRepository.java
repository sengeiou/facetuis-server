package com.facetuis.server.dao.order;

import com.facetuis.server.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {

    List<Order> findByOrder_snIn(List<String> orderSn);


}
