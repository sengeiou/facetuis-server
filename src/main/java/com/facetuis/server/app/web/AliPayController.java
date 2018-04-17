package com.facetuis.server.app.web;

import com.alipay.api.internal.util.AlipaySignature;
import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
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
import java.util.List;

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
    public String aliNotify(){

//        boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, ALIPAY_PUBLIC_KEY, CHARSET) //调用SDK验证签名
//        if(signVerfied){
//            // TODO 验签成功后
//            //按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
//        }else{
//            // TODO 验签失败则记录异常日志，并在response中返回failure.
//        }
        return "SUCCESS";
    }


}
