package com.facetuis.server.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SMSUtils {

    private  static Map<String,Long> mobileList = new HashMap<>();

    public static boolean isSend(String mobile){
        clear();
        if(mobileList.containsKey(mobile)){
            Long sendTime = mobileList.get(mobile);
            Long now = System.currentTimeMillis();
            boolean isEx = now - sendTime > 1000 * 50;
            if( isEx ){
                mobileList.put(mobile,now);
                return true;
            }else{
                return false;
            }
        }else{
            mobileList.put(mobile,System.currentTimeMillis());
            return true;
        }
    }

    private static void clear(){
        if(mobileList.size() >= 5000){
            mobileList = new HashMap<>();
        }
    }


}
