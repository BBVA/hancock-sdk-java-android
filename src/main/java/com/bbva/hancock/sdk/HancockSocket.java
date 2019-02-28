package com.bbva.hancock.sdk;

import com.bbva.hancock.sdk.models.HancockSocketMessage;
import com.bbva.hancock.sdk.models.HancockSocketRequest;
import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;


public class HancockSocket {

    private final WebSocketClient ws;
    private final Map<String, List<Function>> callbackFunctions;

    public HancockSocket(String url) throws URISyntaxException {

        URI wsUri = new URI(url);

        this.callbackFunctions = new HashMap<>();
        this.ws = new WebSocketClient(wsUri)
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

                final List<Function> functionList = callbackFunctions.get(event);

                if (functionList != null) {

                    for (Function function : functionList) {
                        function.apply(obj);
                    }

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

        List<Function> functionList = callbackFunctions.get(event);

        if (functionList == null) {

            functionList = new ArrayList<>();

        }

        functionList.add(function);
        callbackFunctions.put(event, functionList);

    }

    /**
     * Remove a function from the handlers list of an specific event
     * @param event The event which is listened
     * @param function The function to be removed
     */
    public void off(String event, Function function){

        final List<Function> functionList = callbackFunctions.get(event);

        if (functionList != null) {

            for (Function fn : functionList) {

                if (fn.equals(function)) {

                    functionList.remove(fn);

                }

            }

        }

    }

    /**
     * Remove all functions from the handlers list of an specific event
     * @param event The event which is listened
     */
    public void removeAllListeners(String event){

        final List<Function> functionList = callbackFunctions.remove(event);

    }

    /**
     * Remove all handlers for all event types
     */
    public void removeAllListeners(){

        callbackFunctions.clear();

    }

    /**
     * Add new addresses to be listened for event of type "transfers".
     * @param addresses New Addresses to be listened
     */
    public void watchTransfer(ArrayList<String> addresses){
        if (!addresses.isEmpty()) {
            this.sendMessage("watch-transfers", addresses);
        }
    }

    /**
     * Add new addresses to be listened for event of type "transactions".
     * @param addresses New Addresses to be listened
     */
    public void watchTransaction(ArrayList<String> addresses){
        if (!addresses.isEmpty()) {
            this.sendMessage("watch-transactions", addresses);
        }
    }

    /**
     * Add new addresses or alias to be listened for event of type "contracts".
     * @param contracts New address or alias to be listened
     */
    public void watchContract(ArrayList<String> contracts){
        if (!contracts.isEmpty()) {
            this.sendMessage("watch-contracts", contracts);
        }
    }

    /**
     * Stop listening the addresses for event of type "transfers".
     * @param addresses Addresses to stop listening
     */
    public void unwatchTransfer(ArrayList<String> addresses){
        if (!addresses.isEmpty()) {
            this.sendMessage("unwatch-transfers", addresses);
        }
    }

    /**
     * Stop listening the addresses for event of type "transactions".
     * @param addresses Addresses to stop listening
     */
    public void unwatchTransaction(ArrayList<String> addresses){
        if (!addresses.isEmpty()) {
            this.sendMessage("unwatch-transactions", addresses);
        }
    }

    /**
     * Stop listening the contracts for event of type "contracts".
     * @param contracts Contracts to stop listening
     */
    public void unwatchContract(ArrayList<String> contracts){
        if (!contracts.isEmpty()) {
            this.sendMessage("unwatch-contracts", contracts);
        }
    }

    public WebSocketClient getWs() {
        return ws;
    }

    public Map<String, List<Function>> getCallbackFunctions() {
        return callbackFunctions;
    }

    protected void sendMessage(String kind, ArrayList<String> body) {
        HancockSocketRequest message = new HancockSocketRequest(kind, body);
        if (this.ws.isOpen()) {
            Gson gson = new Gson();
            this.ws.send(gson.toJson(message));
        }
    }
}