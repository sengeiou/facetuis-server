package com.facetuis.server.app.web;

import com.alipay.api.internal.util.AlipaySignature;
import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import com.facetuis.server.app.web.request.AlipayNotifyRequest;
import com.facetuis.server.model.product.Product;
import com.facetuis.server.model.user.User;
import com.facetuis.server.service.alipay.AliPayService;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.utils.ProductUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/1.0/alipay")
public class AliPayController extends FacetuisController {

    @Autowired
    private AliPayService aliPayService;

    /**
     * 生成APP手机端的签名后的订单信息字符串
     * @param productId
     * @return
     */
    @RequestMapping( value = "/order/info/{productId}",method = RequestMethod.GET)
    public BaseResponse generateOrder(@PathVariable String productId){
        Product product = ProductUtils.getProduct(productId);
        if(product == null){
            return setErrorResult(400,"商品不存在");
        }
        User user = getUser();
        user = new User();
        user.setMobileNumber("15021439249");
        BaseResult stringBaseResult = aliPayService.generateOrder(product.getAmount(), product.getTitle(), "用户：" + user.getMobileNumber() + "购买了" + product.getTitle());
        return onResult(stringBaseResult);
    }

    @RequestMapping(value = "/notify",method = RequestMethod.POST)
    public String aliNotify(AlipayNotifyRequest request){
        Map<String,String> map = new HashMap<>();
        map.put("notify_time",request.getNotify_time());
        map.put("notify_type",request.getNotify_type());
        map.put("notify_id",request.getNotify_id());
        map.put("app_id",request.getApp_id());
        map.put("charset",request.getCharset());
        map.put("version",request.getVersion());
        map.put("sign_type",request.getSign_type());
        map.put("sign",request.getSign());
        map.put("trade_no",request.getTrade_no());
        map.put("out_trade_no",request.getOut_trade_no());
        map.put("out_biz_no",request.getOut_biz_no());
        map.put("buyer_id",request.getBuyer_id());
        map.put("buyer_logon_id",request.getBuyer_logon_id());
        map.put("seller_id",request.getSeller_id());
        map.put("seller_email",request.getSeller_email());
        map.put("trade_status",request.getTrade_status());
        map.put("total_amount",request.getTotal_amount());
        map.put("receipt_amount",request.getReceipt_amount());
        map.put("invoice_amount",request.getInvoice_amount());
        map.put("buyer_pay_amount",request.getBuyer_pay_amount());
        map.put("point_amount",request.getPoint_amount());
        map.put("refund_fee",request.getRefund_fee());
        map.put("subject",request.getSubject());
        map.put("body",request.getBody());
        map.put("gmt_create",request.getGmt_create());
        map.put("gmt_payment",request.getGmt_payment());
        map.put("gmt_refund",request.getGmt_refund());
        map.put("gmt_close",request.getGmt_close());
        map.put("fund_bill_list",request.getFund_bill_list());
        map.put("passback_params",request.getPassback_params());
        map.put("voucher_detail_list",request.getVoucher_detail_list());


        BaseResult baseResult = aliPayService.checkNotify(map);
        if(baseResult.hasError()){
            return "ERROR";
        }
        return "SUCCESS";
    }
}
