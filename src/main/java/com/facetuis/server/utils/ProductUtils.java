package com.facetuis.server.utils;

import com.facetuis.server.model.product.Product;
import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.List;

public class ProductUtils {
    private static  Product productNo1 = null;
    private static  Product productNo2 = null;

    static {
        if(productNo1 == null){
            productNo1 = new Product();
            productNo1.setUuid("PRODUCT1");
            productNo1.setAmount("198");
            productNo1.setTimeLimit("月");
            productNo1.setTimeLimitValue(1);
            productNo1.setTimeLimitTxt("一个月");
            productNo1.setTitle("会员一个月");

        }

        if(productNo2 == null){
            productNo2 = new Product();
            productNo2.setUuid("PRODUCT2");
            productNo2.setAmount("1680");
            productNo2.setTimeLimit("年");
            productNo2.setTimeLimitValue(1);
            productNo2.setTimeLimitTxt("一年");
            productNo2.setTitle("会员一年");
        }
    }


    public static List<Product> getList(){
        List<Product> list = new ArrayList<>();
        list.add(productNo1);
        list.add(productNo2);
        return list;
    }

    public static Product getProduct(String productId){
        if(productId.equals("PRODUCT1")){
            return productNo1;
        }
        if(productId.equals("PRODUCT2")){
            return productNo2;
        }
        return null;
    }
}
