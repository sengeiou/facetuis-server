package com.facetuis.server.service.payment;

import com.facetuis.server.dao.order.OrderCommisionRepository;
import com.facetuis.server.dao.payment.WithdrawCashRequestRepository;
import com.facetuis.server.dao.user.UserCommisionRepository;
import com.facetuis.server.model.basic.BaseEntity;
import com.facetuis.server.model.pay.WithdrawCashRequest;
import com.facetuis.server.model.user.CashStatus;
import com.facetuis.server.model.user.UserCommision;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.basic.BasicService;
import com.facetuis.server.service.payment.vo.UserCashVO;
import com.facetuis.server.service.pinduoduo.OrderCommisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

import static com.facetuis.server.model.pay.CashStatus.WAIT_CASH;

/**
 * TODO 提现请求是否通过
 */
@Service
public class WithdrawCashRequestService extends BasicService {

    @Autowired
    private WithdrawCashRequestRepository withdrawCashRequestRepository;
    @Autowired
    private OrderCommisionService orderCommisionService;
    @Autowired
    private OrderCommisionRepository orderCommisionRepository;
    @Autowired
    private UserCommisionRepository userCommisionRepository;


    /**
     * 创建提现请求
     * @param withdrawCashRequest
     * @return
     */
    public BaseResult create(WithdrawCashRequest  withdrawCashRequest){
        // 提现人
        String userId = withdrawCashRequest.getUserId();

        //查询提现状态 ==》 如果处于冻结状态，则不能提现
        UserCommision userCommision = userCommisionRepository.findByUserId(withdrawCashRequest.getUserId());
        if(userCommision != null){
            if(userCommision.getCashStatus() == CashStatus.FREEZE){
                return new BaseResult(600,"账户冻结中");
            }
            // 查询可提现金额
            double cash = userCommision.getOrderCash();
            if(cash < withdrawCashRequest.getAmount()){
                return new BaseResult(600,"提现金额不能大于可提现金额");
            }
            withdrawCashRequest.setUuid(UUID.randomUUID().toString());
            withdrawCashRequest.setStatus(WAIT_CASH);
            withdrawCashRequest.setWithdrawTime(new Date());
            withdrawCashRequestRepository.save(withdrawCashRequest);
        }else{
            return new BaseResult(600,"暂不能提现");
        }
        return new BaseResult();
    }

    /**
     * 用户提现页面金额查询
     * @return
     */
    public UserCashVO getCashInfo(String userId){
        UserCashVO vo = new UserCashVO();
        // 查询可提现
        UserCommision userCommision = userCommisionRepository.findByUserId(userId);
        // 订单可提现
        Double orderCash = userCommision.getOrderCash();
        // 升级可提现
        Double updateCash = userCommision.getUpdateCash();
        // 邀请可提现
        Double invitingCash = userCommision.getInvitingCash();
        vo.setAmount(orderCash + updateCash + invitingCash + "");

        // 查询已结算
        vo.setWaitAmount(userCommision.getWaitSettlement() + "");
        // 查询待结算 = 已提现 + 可提现
        double historyCash = userCommision.getOrderCash() + userCommision.getFinishCash();
        vo.setSettlementAmount(historyCash + "");
        return vo;
    }



}
