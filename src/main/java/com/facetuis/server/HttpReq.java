package com.facetuis.server;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;

public class HttpReq {

    public static String sessionid(){
        try {
            Response execute = Request.Get("https://www.bet365.com/?#/AS/B1/").execute();
            String pstk = execute.handleResponse(new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    Header[] cookies = response.getHeaders("Set-Cookie");
                    for (Header header : cookies) {
                        HeaderElement[] elements = header.getElements();
                        for (HeaderElement element : elements) {
                            if (element.getName().equals("pstk")) {
                                return element.getValue();
                            }
                        }
                    }
                    return null;
                }
            });
            return pstk;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }
}
