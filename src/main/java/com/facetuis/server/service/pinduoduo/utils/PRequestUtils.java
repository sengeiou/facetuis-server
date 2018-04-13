package com.facetuis.server.service.pinduoduo.utils;

import com.facetuis.server.service.basic.BaseResult;
import com.facetuis.server.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.logging.Logger;

@Component
public class PRequestUtils {

    private static final Logger logger =  Logger.getLogger(PRequestUtils.class.getName());

    @Value("${pinduoduo.app.api.url}")
    private String url;
    @Value("${pinduoduo.app.id}")
    private String clientId;
    @Value("${pinduoduo.app.secret}")
    private String secret;
    @Value("${pinduoduo.app.api.url}")
    private String apiUrl;


    /**
     * 调用拼多多接口
     * @param api
     * @return
     */
    public  synchronized BaseResult send(String api, SortedMap<String,String> map) {
        map.put("type", api);
        map.put("client_id", clientId);
        map.put("timestamp", System.currentTimeMillis() + "");
        map.put("data_type", "JSON");
        String sign = getSignString(map, secret);
        map.put("sign", sign);
        String url = apiUrl + "?" + getUrlParamsByMap(map);
        try {
            String result = Request.Post(url).execute().returnContent().asString();
            logger.info(result);
            return new BaseResult(result);
        } catch (IOException e) {
            logger.info("调用拼多多接口发生错误::" + api + " | " + e.getMessage());
            e.printStackTrace();
        }
        return new BaseResult();
    }


    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }


    public String getSignString(SortedMap<String, String> parameters, String secret) {
        StringBuffer sb = new StringBuffer();
        sb.append(secret);
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + v);
            }
        }
        sb.append(secret);
        String sign = MD5Utils.MD5(sb.toString()).toUpperCase();
        return sign;
    }

    /**
     * 将map转换成url
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if(!StringUtils.isEmpty(entry.getValue())){
                sb.append(entry.getKey() + "=" + entry.getValue());
                sb.append("&");
            }
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = org.apache.commons.lang3.StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }


}
