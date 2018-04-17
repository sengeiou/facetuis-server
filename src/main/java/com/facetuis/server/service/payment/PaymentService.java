package com.facetuis.server.service.payment;

import com.facetuis.server.dao.payment.PaymentRepository;
import com.facetuis.server.model.pay.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {


    @Autowired
    private PaymentRepository paymentRepository;

    public void save(){
        Payment pay = new Payment();
        pay.setAmount("123123");
        paymentRepository.save(pay);
    }

}
