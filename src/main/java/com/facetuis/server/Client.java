package com.facetuis.server;

import javax.websocket.*;
import java.io.IOException;
import java.nio.ByteBuffer;

@ClientEndpoint
public class Client extends Endpoint{

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("opening");
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

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);
        System.out.println("close");
    }



    @Override
    public void onError(Session session, Throwable throwable) {
        super.onError(session, throwable);
        System.out.println("error");
    }


}
