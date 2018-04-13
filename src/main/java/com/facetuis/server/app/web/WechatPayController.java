package com.facetuis.server.app.web;

import com.facetuis.server.app.web.basic.BaseResponse;
import com.facetuis.server.app.web.basic.FacetuisController;
import com.facetuis.server.model.product.Product;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.wechat.WechatPayService;
import com.facetuis.server.utils.IpUtils;
import com.facetuis.server.utils.ProductUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/1.0/wechat/pay")
public class WechatPayController extends FacetuisController {


    @Autowired
    private WechatPayService wechatPayService;


    /**
     * 微信统一下单
     * @param productid
     * @param request
     * @return
     */
    @RequestMapping(value = "/payment/{productid}",method = RequestMethod.GET)
    public BaseResponse payment(@PathVariable String productid, HttpServletRequest request){
        Product product = ProductUtils.getProduct(productid);
        if(product == null){
            return setErrorResult(600,"没有找到需要支付的产品");
        }
        BaseResult baseResult = wechatPayService.unifiedorder(product.getTitle(), product.getTitle(), product.getAmount(), IpUtils.getIpAddr(request));
        return onResult(baseResult);
    }

}
