package com.facetuis.server;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class TWebMain {

    public static WebSocketClient client;

    public static void main(String[] args) throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        String url = "wss://premws-pt3.365lpodds.com:443/zap/?uid=17120398307604612";
        URI uri = new URI(url);
        Map<String,String> headers = new HashMap<>();
        headers.put("Sec-WebSocket-Extensions","permessage-deflate;client_max_window_bits");
        headers.put("Sec-WebSocket-Protocol","zap-protocol-v1");
        headers.put("Sec-WebSocket-Version","13");
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        headers.put("Upgrade","websocket");
        client = new WebSocketClient(uri,headers) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("1111");
            }

            @Override
            public void onMessage(String message) {
                System.out.println("22222");
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("6666666666");
            }

            @Override
            public void onError(Exception ex) {
                System.out.println("0000000");
            }


        };
        client.connect();
        while(!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)){
            if(client.getReadyState() == WebSocket.READYSTATE.CLOSED){
                System.out.println("连接关闭");
                break;
            }
        }
    }

    public static void send(byte[] bytes){
        client.send(bytes);
    }
}
