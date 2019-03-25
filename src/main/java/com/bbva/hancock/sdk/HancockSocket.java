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
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class HancockSocket {

    private final WebSocketClient ws;
    private final Map<String, List<Function>> callbackFunctions;

    public HancockSocket(final String url) throws URISyntaxException {

        final URI wsUri = new URI(url);

        callbackFunctions = new HashMap<>();
        ws = new WebSocketClient(wsUri) {
            @Override
            public void onOpen(final ServerHandshake handshakedata) {
                executeFunction("open", handshakedata);
            }

            @Override
            public void onMessage(final String message) {
                final Gson gson = new Gson();
                final HancockSocketMessage hsm = gson.fromJson(message, HancockSocketMessage.class);
                executeFunction(hsm.getKind(), hsm);
            }

            @Override
            public void onClose(final int code, final String reason, final boolean remote) {
                executeFunction("close", reason);
            }

            @Override
            public void onError(final Exception ex) {
                executeFunction("error", ex);
            }

            protected void executeFunction(final String event, final Object obj) {

                final List<Function> functionList = callbackFunctions.get(event);

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

    }

    /**
     * Add new addresses to be listened for event of type "transfers".
     *
     * @param addresses New Addresses to be listened
     */
    public void watchTransfer(final ArrayList<String> addresses) {
        if (!addresses.isEmpty()) {
            sendMessage("watch-transfers", addresses);
        }
    }

    /**
     * Add new addresses to be listened for event of type "transactions".
     *
     * @param addresses New Addresses to be listened
     */
    public void watchTransaction(final ArrayList<String> addresses) {
        if (!addresses.isEmpty()) {
            sendMessage("watch-transactions", addresses);
        }
    }

    /**
     * Add new addresses or alias to be listened for event of type "contracts".
     *
     * @param contracts New address or alias to be listened
     */
    public void watchContract(final ArrayList<String> contracts) {
        if (!contracts.isEmpty()) {
            sendMessage("watch-contracts", contracts);
        }
    }

    /**
     * Stop listening the addresses for event of type "transfers".
     *
     * @param addresses Addresses to stop listening
     */
    public void unwatchTransfer(final ArrayList<String> addresses) {
        if (!addresses.isEmpty()) {
            sendMessage("unwatch-transfers", addresses);
        }
    }

    /**
     * Stop listening the addresses for event of type "transactions".
     *
     * @param addresses Addresses to stop listening
     */
    public void unwatchTransaction(final ArrayList<String> addresses) {
        if (!addresses.isEmpty()) {
            sendMessage("unwatch-transactions", addresses);
        }
    }

    /**
     * Stop listening the contracts for event of type "contracts".
     *
     * @param contracts Contracts to stop listening
     */
    public void unwatchContract(final ArrayList<String> contracts) {
        if (!contracts.isEmpty()) {
            sendMessage("unwatch-contracts", contracts);
        }
    }

    public WebSocketClient getWs() {
        return ws;
    }

    public Map<String, List<Function>> getCallbackFunctions() {
        return callbackFunctions;
    }

    protected void sendMessage(final String kind, final ArrayList<String> body) {
        final HancockSocketRequest message = new HancockSocketRequest(kind, body);
        if (ws.isOpen()) {
            final Gson gson = new Gson();
            ws.send(gson.toJson(message));
        }
    }
}
