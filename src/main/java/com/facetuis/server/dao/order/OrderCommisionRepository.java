package com.facetuis.server.dao.order;

import com.facetuis.server.model.order.OrderCommision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCommisionRepository extends JpaRepository<OrderCommision,String> {


    OrderCommision findByOrderSn(String orderSn);


}
