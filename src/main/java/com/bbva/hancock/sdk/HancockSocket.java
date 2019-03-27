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

    public HancockSocket(final String url) throws URISyntaxException {

        final URI wsUri = new URI(url);

        errorFunctions = new ArrayList<>();
        transactionFunctions = new HashMap<>();
        contractsFunctions = new HashMap<>();
        callbackFunctions = new HashMap<>();
        ws = new WebSocketClient(wsUri) {
            @Override
            public void onOpen(final ServerHandshake handshakedata) {
                executeFunction("ready", handshakedata);
            }

            @Override
            public void onMessage(final String message) {
                final Gson gson = new Gson();
                final HancockSocketMessage hsm = gson.fromJson(message, HancockSocketMessage.class);
                switch (HancockSocketMessageResponseKind.valueOf(hsm.getKind())) {
                    case TRANSFER:
                    case TRANSACTION:
                    case SMARTCONTRACTTRANSACTION:
                        final HancockSocketTransactionEvent transactionEvent = gson.fromJson(message, HancockSocketTransactionEvent.class);
                        executeEventFunction(transactionEvent, transactionFunctions);
                        break;
                    case SMARTCONTRACTEVENT:
                        final HancockSocketContractEvent contractEvent = gson.fromJson(message, HancockSocketContractEvent.class);
                        executeEventFunction(contractEvent, contractsFunctions);
                        break;
                    default:
                        executeFunction(hsm.getKind(), hsm);
                }
            }

            @Override
            public void onClose(final int code, final String reason, final boolean remote) {
                executeFunction("close", reason);
            }

            @Override
            public void onError(final Exception ex) {
                executeErrorFunction(ex);
            }

            protected void executeErrorFunction(final Exception obj) {
                for (final Function function : errorFunctions) {
                    function.apply(obj);
                }
            }

            protected void executeFunction(final String event, final Object obj) {

                final List<Function> functionList = callbackFunctions.get(event);

                if (functionList != null) {
                    for (final Function function : functionList) {
                        function.apply(obj);
                    }
                }
            }

            protected <T extends HancockSocketMessage> void executeEventFunction(final T obj, final Map<String, List<Function<T, Void>>> mapFunctions) {

                final List<Function<T, Void>> functionList = mapFunctions.get(obj.getKind());

                if (functionList != null) {
                    for (final Function function : functionList) {
                        function.apply(obj);
                    }
                }
            }
        };

        ws.connect();
    }

    /**
     * Add a new function to be called when a message is received from the socket fot a specific event
     *
     * @param event    The event which is listened
     * @param function The function to be called
     */
    public void on(final String event, final Function function) {

        List<Function> functionList = callbackFunctions.get(event);

        if (functionList == null) {
            functionList = new ArrayList<>();
        }

        functionList.add(function);
        callbackFunctions.put(event, functionList);

    }

    /**
     * Add a new function to be called when a exception is received from the socket
     *
     * @param function The function to be called
     */
    public void onError(final Function<Exception, Void> function) {

        errorFunctions.add(function);

    }

    /**
     * Add a new function to be called when a message is received from the socket for a specific event of transaction
     *
     * @param event    The event which is listened (transfer, transaction or contract-transaction)
     * @param function The function to be called
     */
    public void onTransaction(final String event, final Function<HancockSocketTransactionEvent, Void> function) {

        onEvent(event, function, transactionFunctions);

    }

    /**
     * Add a new function to be called when a message is received from the socket for a specific event of contract events
     *
     * @param event    The event which is listened (contract-event)
     * @param function The function to be called
     */
    public void onContractEvent(final String event, final Function<HancockSocketContractEvent, Void> function) {

        onEvent(event, function, contractsFunctions);

    }

    private <T extends HancockSocketMessage> void onEvent(final String event, final Function<T, Void> function, final Map<String, List<Function<T, Void>>> mapFunctions) {

        List<Function<T, Void>> functionList = mapFunctions.get(event);

        if (functionList == null) {
            functionList = new ArrayList<>();
        }

        functionList.add(function);
        mapFunctions.put(event, functionList);
    }

    /**
     * Remove a function from the handlers list of an specific event
     *
     * @param event    The event which is listened
     * @param function The function to be removed
     */
    public void off(final String event, final Function function) {

        final List<Function> functionList = callbackFunctions.get(event);

        if (functionList != null) {
            for (final Function fn : functionList) {
                if (fn.equals(function)) {
                    functionList.remove(fn);
                }
            }
        }
    }

    /**
     * Remove a function from the handlers list of an specific event
     *
     * @param event    The event which is listened
     * @param function The function to be removed
     */
    public void offTransaction(final String event, final Function<HancockSocketTransactionEvent, Void> function) {

        offEvent(event, function, transactionFunctions);

    }

    /**
     * Remove a function from the handlers list of an specific event
     *
     * @param event    The event which is listened
     * @param function The function to be removed
     */
    public void offContract(final String event, final Function<HancockSocketContractEvent, Void> function) {

        offEvent(event, function, contractsFunctions);
    }

    /**
     * Remove a function from the handlers list of an specific event
     *
     * @param event    The event which is listened
     * @param function The function to be removed
     */
    private <T extends HancockSocketMessage> void offEvent(final String event, final Function<T, Void> function, final Map<String, List<Function<T, Void>>> mapFunctions) {

        final List<Function<T, Void>> functionList = mapFunctions.get(event);

        if (functionList != null) {
            for (final Function fn : functionList) {
                if (fn.equals(function)) {
                    functionList.remove(fn);
                }
            }
        }
    }

    /**
     * Remove a function from the handlers list of error events
     *
     * @param function The function to be removed
     */
    public void offError(final Function<Exception, Void> function) {

        if (errorFunctions != null) {
            for (final Function fn : errorFunctions) {
                if (fn.equals(function)) {
                    errorFunctions.remove(fn);
                }
            }
        }
    }

    /**
     * Remove all functions from the handlers list of an specific event
     *
     * @param event The event which is listened
     */
    public void removeAllListeners(final String event) {

        callbackFunctions.remove(event);

    }

    /**
     * Remove all handlers for all event types
     */
    public void removeAllListeners() {

        callbackFunctions.clear();
        transactionFunctions.clear();
        contractsFunctions.clear();
        errorFunctions.clear();

    }

    /**
     * Add new addresses to be listened for event of type "transfers".
     *
     * @param addresses New Addresses to be listened
     */
    public void watchTransfer(final List<String> addresses) {
        if (!addresses.isEmpty()) {
            sendMessage(HancockSocketMessageRequestKind.WATCHTRANSFER.getKind(), addresses);
        }
    }

    /**
     * Add new addresses to be listened for event of type "transactions".
     *
     * @param addresses New Addresses to be listened
     */
    public void watchTransaction(final List<String> addresses) {
        if (!addresses.isEmpty()) {
            sendMessage(HancockSocketMessageRequestKind.WATCHTRANSACTION.getKind(), addresses);
        }
    }

    /**
     * Add new addresses or alias to be listened for event of type "contract-transaction".
     *
     * @param contracts New address or alias to be listened
     */
    public void watchContractTransaction(final List<String> contracts) {
        if (!contracts.isEmpty()) {
            sendMessage(HancockSocketMessageRequestKind.WATCHSMARTCONTRACTTRANSACTION.getKind(), contracts);
        }
    }

    /**
     * Add new addresses or alias to be listened for event of type "contract-event".
     *
     * @param contracts New address or alias to be listened
     */
    public void watchContractEvent(final List<String> contracts) {
        if (!contracts.isEmpty()) {
            sendMessage(HancockSocketMessageRequestKind.WATCHSMARTCONTRACTEVENT.getKind(), contracts);
        }
    }

    /**
     * Stop listening the addresses for event of type "transfers".
     *
     * @param addresses Addresses to stop listening
     */
    public void unwatchTransfer(final List<String> addresses) {
        if (!addresses.isEmpty()) {
            sendMessage(HancockSocketMessageRequestKind.UNWATCHTRANSFER.getKind(), addresses);
        }
    }

    /**
     * Stop listening the addresses for event of type "transactions".
     *
     * @param addresses Addresses to stop listening
     */
    public void unwatchTransaction(final List<String> addresses) {
        if (!addresses.isEmpty()) {
            sendMessage(HancockSocketMessageRequestKind.UNWATCHTRANSACTION.getKind(), addresses);
        }
    }

    /**
     * Stop listening the contracts for event of type "contracts".
     *
     * @param contracts Contracts to stop listening
     */
    public void unwatchContractTransaction(final List<String> contracts) {
        if (!contracts.isEmpty()) {
            sendMessage(HancockSocketMessageRequestKind.UNWATCHSMARTCONTRACTTRANSACTION.getKind(), contracts);
        }
    }

    /**
     * Stop listening the contracts for event of type "contracts".
     *
     * @param contracts Contracts to stop listening
     */
    public void unwatchContractEvent(final List<String> contracts) {
        if (!contracts.isEmpty()) {
            sendMessage(HancockSocketMessageRequestKind.UNWATCHSMARTCONTRACTEVENT.getKind(), contracts);
        }
    }

    public WebSocketClient getWs() {
        return ws;
    }

    public Map<String, List<Function>> getCallbackFunctions() {
        return callbackFunctions;
    }

    public Map<String, List<Function<HancockSocketTransactionEvent, Void>>> getTransactionFunctions() {
        return transactionFunctions;
    }

    public Map<String, List<Function<HancockSocketContractEvent, Void>>> getContractsFunctions() {
        return contractsFunctions;
    }

    public List<Function<Exception, Void>> getErrorFunctions() {
        return errorFunctions;
    }

    protected void sendMessage(final String kind, final List<String> body) {
        final HancockSocketRequest message = new HancockSocketRequest(kind, body);
        if (ws.isOpen()) {
            final Gson gson = new Gson();
            ws.send(gson.toJson(message));
        }
    }
}
