package com.bbva.hancock.sdk;

import com.bbva.hancock.sdk.models.socket.*;
import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class HancockSocket {

    private final WebSocketClient ws;
    private final List<Function<Exception, Void>> errorFunctions;
    private final Map<String, List<Function>> callbackFunctions;
    private final Map<String, List<Function<HancockSocketTransactionEvent, Void>>> transactionFunctions;
    private final Map<String, List<Function<HancockSocketContractEvent, Void>>> contractsFunctions;

    public HancockSocket(String url) throws URISyntaxException {

        URI wsUri = new URI(url);

        this.errorFunctions = new ArrayList<>();
        this.transactionFunctions = new HashMap<>();
        this.contractsFunctions = new HashMap<>();
        this.callbackFunctions = new HashMap<>();
        this.ws = new WebSocketClient(wsUri)
        {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                executeFunction("ready", handshakedata);
            }

            @Override
            public void onMessage(String message) {
                Gson gson = new Gson();
                HancockSocketMessage hsm = gson.fromJson(message, HancockSocketMessage.class);
                switch (HancockSocketMessageResponseKind.valueOf(hsm.getKind())) {
                    case TRANSFER:
                    case TRANSACTION:
                    case SMARTCONTRACTTRANSACTION:
                        HancockSocketTransactionEvent transactionEvent = gson.fromJson(message, HancockSocketTransactionEvent.class);
                        executeEventFunction(transactionEvent, transactionFunctions);
                        break;
                    case SMARTCONTRACTEVENT:
                        HancockSocketContractEvent contractEvent = gson.fromJson(message, HancockSocketContractEvent.class);
                        executeEventFunction(contractEvent, contractsFunctions);
                        break;
                    default:
                        executeFunction(hsm.getKind(), hsm);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                executeFunction("close", reason);
            }

            @Override
            public void onError(Exception ex) {
                executeErrorFunction(ex);
            }

            protected void executeErrorFunction(Exception obj) {
                for (Function function : errorFunctions) {
                    function.apply(obj);
                }
            }

            protected void executeFunction(String event, Object obj) {

                final List<Function> functionList = callbackFunctions.get(event);

                if (functionList != null) {
                    for (Function function : functionList) {
                        function.apply(obj);
                    }
                }
            }

            protected <T extends HancockSocketMessage> void executeEventFunction(T obj,  Map<String, List<Function<T, Void>>> mapFunctions) {

                final List<Function<T, Void>> functionList = mapFunctions.get(obj.getKind());

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
     * Add a new function to be called when a exception is received from the socket
     * @param function The function to be called
     */
    public void onError(Function<Exception, Void> function){

        errorFunctions.add(function);

    }

    /**
     * Add a new function to be called when a message is received from the socket for a specific event of transaction
     * @param event The event which is listened (transfer, transaction or contract-transaction)
     * @param function The function to be called
     */
    public void onTransaction(String event, Function<HancockSocketTransactionEvent, Void> function){

        onEvent(event, function, transactionFunctions);

    }

    /**
     * Add a new function to be called when a message is received from the socket for a specific event of contract events
     * @param event The event which is listened (contract-event)
     * @param function The function to be called
     */
    public void onContractEvent(String event, Function<HancockSocketContractEvent, Void> function){

        onEvent(event, function, contractsFunctions);

    }

    private <T extends HancockSocketMessage> void onEvent(String event, Function<T, Void> function, Map<String, List<Function<T, Void>>> mapFunctions){

        List<Function<T, Void>> functionList = mapFunctions.get(event);

        if (functionList == null) {
            functionList = new ArrayList<>();
        }

        functionList.add(function);
        mapFunctions.put(event, functionList);
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
     * Remove a function from the handlers list of an specific event
     * @param event The event which is listened
     * @param function The function to be removed
     */
    public void offTransaction(String event, Function<HancockSocketTransactionEvent, Void> function){

        offEvent(event, function, transactionFunctions);

    }

    /**
     * Remove a function from the handlers list of an specific event
     * @param event The event which is listened
     * @param function The function to be removed
     */
    public void offContract(String event, Function<HancockSocketContractEvent, Void> function){

        offEvent(event, function, contractsFunctions);
    }

    /**
     * Remove a function from the handlers list of an specific event
     * @param event The event which is listened
     * @param function The function to be removed
     */
    private <T extends HancockSocketMessage> void offEvent(String event, Function<T, Void> function, Map<String, List<Function<T, Void>>> mapFunctions){

        final List<Function<T, Void>> functionList = mapFunctions.get(event);

        if (functionList != null) {
            for (Function fn : functionList) {
                if (fn.equals(function)) {
                    functionList.remove(fn);
                }
            }
        }
    }

    /**
     * Remove a function from the handlers list of error events
     * @param function The function to be removed
     */
    public void offError(Function<Exception, Void> function){

        if (errorFunctions != null) {
            for (Function fn : errorFunctions) {
                if (fn.equals(function)) {
                    errorFunctions.remove(fn);
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
        transactionFunctions.clear();
        contractsFunctions.clear();
        errorFunctions.clear();

    }

    /**
     * Add new addresses to be listened for event of type "transfers".
     * @param addresses New Addresses to be listened
     */
    public void watchTransfer(List<String> addresses){
        if (!addresses.isEmpty()) {
            this.sendMessage(HancockSocketMessageRequestKind.WATCHTRANSFER.getKind(), addresses);
        }
    }

    /**
     * Add new addresses to be listened for event of type "transactions".
     * @param addresses New Addresses to be listened
     */
    public void watchTransaction(List<String> addresses){
        if (!addresses.isEmpty()) {
            this.sendMessage(HancockSocketMessageRequestKind.WATCHTRANSACTION.getKind(), addresses);
        }
    }

    /**
     * Add new addresses or alias to be listened for event of type "contracts".
     * @param contracts New address or alias to be listened
     */
    public void watchContract(List<String> contracts){
        if (!contracts.isEmpty()) {
            this.sendMessage(HancockSocketMessageRequestKind.WATCHSMARTCONTRACT.getKind(), contracts);
        }
    }

    /**
     * Add new addresses or alias to be listened for event of type "contract-transaction".
     * @param contracts New address or alias to be listened
     */
    public void watchContractTransaction(List<String> contracts){
        if (!contracts.isEmpty()) {
            this.sendMessage(HancockSocketMessageRequestKind.WATCHSMARTCONTRACTTRANSACTION.getKind(), contracts);
        }
    }

    /**
     * Add new addresses or alias to be listened for event of type "contract-event".
     * @param contracts New address or alias to be listened
     */
    public void watchContractEvent(List<String> contracts){
        if (!contracts.isEmpty()) {
            this.sendMessage(HancockSocketMessageRequestKind.WATCHSMARTCONTRACTEVENT.getKind(), contracts);
        }
    }

    /**
     * Stop listening the addresses for event of type "transfers".
     * @param addresses Addresses to stop listening
     */
    public void unwatchTransfer(List<String> addresses){
        if (!addresses.isEmpty()) {
            this.sendMessage(HancockSocketMessageRequestKind.UNWATCHTRANSFER.getKind(), addresses);
        }
    }

    /**
     * Stop listening the addresses for event of type "transactions".
     * @param addresses Addresses to stop listening
     */
    public void unwatchTransaction(List<String> addresses){
        if (!addresses.isEmpty()) {
            this.sendMessage(HancockSocketMessageRequestKind.UNWATCHTRANSACTION.getKind(), addresses);
        }
    }

    /**
     * Stop listening the contracts for event of type "contracts".
     * @param contracts Contracts to stop listening
     */
    public void unwatchContract(List<String> contracts){
        if (!contracts.isEmpty()) {
            this.sendMessage(HancockSocketMessageRequestKind.UNWATCHSMARTCONTRACT.getKind(), contracts);
        }
    }

    /**
     * Stop listening the contracts for event of type "contracts".
     * @param contracts Contracts to stop listening
     */
    public void unwatchContractTransaction(List<String> contracts){
        if (!contracts.isEmpty()) {
            this.sendMessage(HancockSocketMessageRequestKind.UNWATCHSMARTCONTRACTTRANSACTION.getKind(), contracts);
        }
    }

    /**
     * Stop listening the contracts for event of type "contracts".
     * @param contracts Contracts to stop listening
     */
    public void unwatchContractEvent(List<String> contracts){
        if (!contracts.isEmpty()) {
            this.sendMessage(HancockSocketMessageRequestKind.UNWATCHSMARTCONTRACTEVENT.getKind(), contracts);
        }
    }

    public WebSocketClient getWs() {
        return ws;
    }

    public Map<String, List<Function>> getCallbackFunctions() {
        return callbackFunctions;
    }

    protected void sendMessage(String kind, List<String> body) {
        HancockSocketRequest message = new HancockSocketRequest(kind, body);
        if (this.ws.isOpen()) {
            Gson gson = new Gson();
            this.ws.send(gson.toJson(message));
        }
    }
}