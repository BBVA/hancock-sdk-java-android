package com.bbva.hancock.sdk;

import com.bbva.hancock.sdk.models.HancockSocketMessage;
import com.bbva.hancock.sdk.models.HancockSocketRequest;
import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class HancockSocket {

    private WebSocketClient ws;
    private Map<String, Function> callbackFunctions;

    public HancockSocket(String url) throws URISyntaxException {

        this.callbackFunctions = new HashMap<>();
        this.ws = new WebSocketClient(new URI(url))
        {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                executeFunction("open", handshakedata);
            }

            @Override
            public void onMessage(String message) {
                Gson gson = new Gson();
                HancockSocketMessage hsm = gson.fromJson(message, HancockSocketMessage.class);
                executeFunction(hsm.getKind(), hsm);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                executeFunction("close", reason);
            }

            @Override
            public void onError(Exception ex) {
                executeFunction("error", ex);
            }

            protected void executeFunction(String event, Object obj) {
                Function function = callbackFunctions.get(event);
                if (function != null) {
                    function.apply(obj);
                }
            }
        };

        this.ws.connect();
    }

    /**
     * Add a new function to be called when a message is received from the socket fot a specific event
     * @param event The event which is listened
     * @param function The function to be called
     */
    public void on(String event, Function function){
        this.callbackFunctions.put(event, function);
    }

    /**
     * Add new addresses to be listened for event of type "transfers".
     * @param addresses New Addresses to be listened
     */
    public void addTransfer(ArrayList<String> addresses){
        if (!addresses.isEmpty()) {
            this.sendMessage("watch-transfers", addresses);
        }
    }

    /**
     * Add new addresses to be listened for event of type "transactions".
     * @param addresses New Addresses to be listened
     */
    public void addTransaction(ArrayList<String> addresses){
        if (!addresses.isEmpty()) {
            this.sendMessage("watch-transactions", addresses);
        }
    }

    /**
     * Add new addresses or alias to be listened for event of type "contracts".
     * @param contracts New address or alias to be listened
     */
    public void addContract(ArrayList<String> contracts){
        if (!contracts.isEmpty()) {
            this.sendMessage("watch-contracts", contracts);
        }
    }

    public WebSocketClient getWs() {
        return ws;
    }

    public Map<String, Function> getCallbackFunctions() {
        return callbackFunctions;
    }

    protected void sendMessage(String kind, ArrayList<String> body) {
//        HancockSocketMessage message = this.getMessageFormat(kind, body);
        HancockSocketRequest message = new HancockSocketRequest(kind, body);
        if (this.ws.isOpen()) {
            Gson gson = new Gson();
            this.ws.send(gson.toJson(message));
        }
    }

//    private HancockSocketMessage getMessageFormat(String kind, ArrayList<String> body) {
//
//    }
}