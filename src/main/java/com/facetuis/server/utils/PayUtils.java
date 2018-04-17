package com.facetuis.server.utils;

public class PayUtils {


    /**
     *
     * 生成支付订单号
     * @return
     */
    public static  String getTradeNo(){
        long now = System.nanoTime();
        StringBuilder no = new StringBuilder();
        no.append(now);
        no.append(RandomUtils.randomNumber(6));
        return no.toString();
    }





    public static void main(String[] args) {
        System.out.printf(getTradeNo());
    }
}
