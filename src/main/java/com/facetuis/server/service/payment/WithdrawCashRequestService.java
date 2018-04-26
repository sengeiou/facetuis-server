package com.facetuis.server.service.payment;

import com.facetuis.server.dao.order.OrderCommisionRepository;
import com.facetuis.server.dao.payment.WithdrawCashRequestRepository;
import com.facetuis.server.model.basic.BaseEntity;
import com.facetuis.server.model.pay.WithdrawCashRequest;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.basic.BasicService;
import com.facetuis.server.service.payment.vo.UserCashVO;
import com.facetuis.server.service.pinduoduo.OrderCommisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WithdrawCashRequestService extends BasicService {

    @Autowired
    private WithdrawCashRequestRepository withdrawCashRequestRepository;

    @Autowired
    private OrderCommisionService orderCommisionService;

    @Autowired
    private OrderCommisionRepository orderCommisionRepository;


    public BaseResult create(WithdrawCashRequest  withdrawCashRequest){
        // 查询可提现金额
        String userId = withdrawCashRequest.getUserId();// 提现人



        return null;
    }

    /**
     * 用户提现页面金额查询
     * @return
     */
    public UserCashVO getCashInfo(){
        // 查询可提现

        // 查询已结算

        // 查询待结算

        return null;
    }
}
