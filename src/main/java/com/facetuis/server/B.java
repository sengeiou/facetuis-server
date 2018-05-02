package com.facetuis.server;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class B {

    public static WebSocketClient client;
    public static void main(String[] args) throws URISyntaxException {
        Map<String,String> map = new HashMap<>();
        map.put("Sec-WebSocket-Extensions",  "permessage-deflate;client_max_window_bits");
        map.put("Sec-WebSocket-Protocol","zap-protocol-v1");
        map.put("Sec-WebSocket-Version","13");
        map.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        map.put("Upgrade","websocket");

        client = new WebSocketClient(new URI("wss://premws-pt3.365lpodds.com:443/zap/?uid=17120398307604612"),new Draft_17(),map,10000) {

            @Override
            public void onOpen(ServerHandshake arg0) {
                System.out.println("打开链接");
            }

            @Override
            public void onMessage(String arg0) {
                System.out.println("收到消息"+arg0);
            }

            @Override
            public void onError(Exception arg0) {
                arg0.printStackTrace();
                System.out.println("发生错误已关闭");
            }

            @Override
            public void onClose(int arg0, String arg1, boolean arg2) {
                System.out.println("链接已关闭");
            }

            @Override
            public void onMessage(ByteBuffer bytes) {
                try {
                    System.out.println(new String(bytes.array(),"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }


        };

        client.connect();

        while(!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)){

        }
    }
}
