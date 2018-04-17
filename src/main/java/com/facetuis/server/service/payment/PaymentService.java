package com.facetuis.server.service.payment;

import com.facetuis.server.dao.payment.PaymentRepository;
import com.facetuis.server.model.pay.PayStatus;
import com.facetuis.server.model.pay.PayType;
import com.facetuis.server.model.pay.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {


    @Autowired
    private PaymentRepository paymentRepository;

    public void save(String tradeNo, String amount, PayStatus payStatus, PayType payType){
        Payment pay = new Payment();
        pay.setAmount(amount);//价格
        pay.setTradeNo(tradeNo);//订单号
        pay.setPayStatus(payStatus);//支付状态
        pay.setPayType(payType); //




        //设置主键
        pay.setUuid(UUID.randomUUID().toString());
        paymentRepository.save(pay);
    }

}
