package com.facetuis.server.app.web;

import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import com.facetuis.server.service.payment.WithdrawCashRequestService;
import com.facetuis.server.utils.NeedLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/1.0/cash")
public class CashController extends FacetuisController {

    @Autowired
    private WithdrawCashRequestService withdrawCashRequestService;

    @RequestMapping(method = RequestMethod.GET)
    @NeedLogin(needLogin = true)
    public BaseResponse index(){


        return successResult();
    }

}
