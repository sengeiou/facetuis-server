package com.facetuis.server.service.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.service.payment.PaymentService;
import com.facetuis.server.utils.PayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.alipay.api.AlipayConstants.CHARSET;
import static com.alipay.api.AlipayConstants.CHARSET_UTF8;

@Service
public class AliPayService {

    @Autowired
    private PaymentService paymentService;

    @Value("${alipay.app.id}")
    private String APP_ID;
    @Value("${alipay.app.private}")
    private String APP_PRIVATE_KEY;
    @Value("${alipay.server.url}")
    private String serverUrl;
    private String format = "json";
    @Value("${alipay.public}")
    private String ALIPAY_PUBLIC_KEY;
    @Value("${alipay.notify.url}")
    private String notfiyUrl;

    /**
     * 生成订单信息签名字符串
     * @param total_amount
     * @param subject
     * @param body
     * @return
     */
    public BaseResult<String> generateOrder(String total_amount,String subject,String body) {
        // 保存支付信息到数据库

        paymentService.save();


        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, APP_ID, APP_PRIVATE_KEY, format, CHARSET_UTF8, ALIPAY_PUBLIC_KEY, "RSA");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(body);
        model.setSubject(subject);
        model.setOutTradeNo(PayUtils.getTradeNo());
        model.setTimeoutExpress("30m");
        model.setTotalAmount(total_amount);
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(notfiyUrl);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            return new BaseResult(response.getBody());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return new BaseResult();
    }


    public BaseResult checkNotify(Map<String,String> map){
        boolean signVerified = false; //调用SDK验证签名
        try {
            signVerified = AlipaySignature.rsaCheckV1(map, ALIPAY_PUBLIC_KEY, CHARSET);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(signVerified){
            // TODO 验签成功后
            // 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure

        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.

        }
        return new BaseResult(600,"签名校验失败");
    }



}
