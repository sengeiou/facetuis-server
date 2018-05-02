package com.facetuis.server;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

@ClientEndpoint
public class A {

    public static void main(String[] args) throws IOException, DeploymentException, InterruptedException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri ="ws://localhost:8077/web/SimpleWebSocket";
        System.out.println("Connecting to  "+ uri);


        Session session = container.connectToServer(A.class, URI.create(uri));
        session.getBasicRemote().sendText("这是从java客户端发送的消息 。。。");
        Thread.sleep(1000);
        session.close();
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("open ... ");
        String message =  "%s%sP%s__time,S_%%s%s";
        message = String.format(message, TWebMain.decodeUnicode("\\\\u23") ,TWebMain.decodeUnicode("\\\\u03"),TWebMain.decodeUnicode("\\\\u01"),TWebMain.decodeUnicode("\\\\u00"));
        message = String.format(message, HttpReq.sessionid());
        ByteBuffer bb = ByteBuffer.wrap(message.getBytes());
        try {
            session.getBasicRemote().sendBinary(bb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println(message);
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
}
