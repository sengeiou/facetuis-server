package com.facetuis.server;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.HandshakeResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Config  extends ClientEndpointConfig.Configurator {

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        super.beforeRequest(headers);
        headers.put("Sec-WebSocket-Extensions", Arrays.asList("permessage-deflate;client_max_window_bits"));
        headers.put("Sec-WebSocket-Protocol",Arrays.asList("zap-protocol-v1"));
        headers.put("Sec-WebSocket-Version",Arrays.asList("13"));
        headers.put("User-Agent",Arrays.asList("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"));
        headers.put("Upgrade",Arrays.asList("websocket"));
    }

    @Override
    public void afterResponse(HandshakeResponse handshakeResponse) {
        super.afterResponse(handshakeResponse);
    }


}
