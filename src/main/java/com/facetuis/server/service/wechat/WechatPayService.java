package com.facetuis.server.service.wechat;

import com.facetuis.server.model.pay.PayStatus;
import com.facetuis.server.model.pay.PayType;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.payment.PaymentService;
import com.facetuis.server.utils.PayCommonUtil;
import com.facetuis.server.utils.PayUtils;
import com.facetuis.server.utils.RandomUtils;
import com.facetuis.server.utils.XmlUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.apache.http.client.fluent.Request.*;

@Service
public class WechatPayService {

    @Autowired
    private PaymentService paymentService;
    @Value("${wechat.app.id}")
    private String appid;
    @Value("${wechat.pay.mch.id}")
    private String mchid;
    @Value("${wechat.app.secret}")
    private String key;
    @Value("${wechat.pay.notify.url}")
    private String notifyUrl;
    @Value("${wechat.pay.unifiedorder.url}")
    private String unifiedorderUrl;


    public BaseResult unifiedorder(String body,String detail,String total_price,String ip){
        //生成订单号码   //body 就是title 商品标题 detail 描述
        String tradeNo=PayUtils.getTradeNo();
       // paymentService.save(tradeNo,total_amount, PayStatus.WAIT_PAY, PayType.ALIPAY);
        paymentService.save(tradeNo,body,total_price,PayStatus.WAIT_PAY,PayType.WECHAT_PAY);

        BaseResult baseResult  = new BaseResult();
        SortedMap<Object,Object> map = new TreeMap<>();
        map.put("appid",appid);
        map.put("mch_id",mchid);
        map.put("nonce_str", RandomUtils.random(24));
        map.put("body",body);
        map.put("detail",detail);
        map.put("out_trade_no", PayUtils.getTradeNo());
        map.put("total_fee",total_price);
        map.put("spbill_create_ip",ip);
        map.put("notify_url",notifyUrl);
        map.put("trade_type","APP");
        String sign = PayCommonUtil.createSign(map,key);
        map.put("sign",sign);
        String xml = PayCommonUtil.getRequestXml(map);
        try {
            String respone = Post(unifiedorderUrl).bodyString(xml, ContentType.APPLICATION_XML).execute().returnContent().asString();
            Map<String,String> result = XmlUtils.doXMLParse(respone);
            SortedMap<Object, Object> parameterMap2 = new TreeMap<Object, Object>();
            parameterMap2.put("appid", appid);
            parameterMap2.put("partnerid",mchid);
            parameterMap2.put("prepayid", map.get("prepay_id"));
            parameterMap2.put("package", "Sign=WXPay");
            parameterMap2.put("noncestr", RandomUtils.random(24));
            //本来生成的时间戳是13位，但是ios必须是10位，所以截取了一下
            parameterMap2.put("timestamp", Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0,10)));
            String sign2 = PayCommonUtil.createSign(parameterMap2,key);
            parameterMap2.put("sign", sign2);
            baseResult.setResult(parameterMap2);
            return  baseResult;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return new BaseResult();
    }

    public BaseResult<String> checkNotify(String xml) {
        try {
            Map map = XmlUtils.doXMLParse(xml);
            if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
                //PayCommonUtil.isTenpaySign(map,key); 验证签名
                String resWX = PayCommonUtil.setXML("SUCCESS", "OK");
                return new BaseResult<String>(resWX);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BaseResult(600,"处理失败");
    }




}
