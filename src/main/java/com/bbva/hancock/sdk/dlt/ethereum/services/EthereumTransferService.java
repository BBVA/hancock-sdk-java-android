package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.HancockSocket;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransactionAdaptResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.exception.HancockTypeErrorEnum;
import com.bbva.hancock.sdk.models.TransactionConfig;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.List;
import java.util.function.Function;

import static com.bbva.hancock.sdk.Common.*;

public class EthereumTransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EthereumTransferService.class);
    private static final MediaType CONTENT_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private final HancockConfig config;

    private final EthereumTransactionService transactionClient;

    public EthereumTransferService(final HancockConfig config, final EthereumTransactionService transactionClient) {
        this.config = config;
        this.transactionClient = transactionClient;
    }

    /**
     * Send ethers between two accounts
     *
     * @param tx       Data of the transaction (sender address, receiver addres, amount of ether, data)
     * @param txConfig Configuration of how the transaction will be send to the network
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTransactionResponse send(final EthereumTransferRequest tx, final TransactionConfig txConfig) throws Exception {
        final EthereumTransaction rawtx = adaptTransfer(tx);
        return transactionClient.send(rawtx, txConfig);
    }

    /**
     * Create a websocket subscription to watch transactions of type "transfer" in the network
     *
     * @param addresses An array of address that will be added to the watch list
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribe(final List<String> addresses) throws HancockException {
        return subscribe(addresses, "", null);
    }

    /**
     * Create a websocket subscription to watch transactions of type "transfer" in the network
     *
     * @param addresses An array of address that will be added to the watch list
     * @param consumer  A consumer plugin previously configured in hancock that will handle each received event
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribe(final List<String> addresses, final String consumer) throws HancockException {
        return subscribe(addresses, consumer, null);
    }

    /**
     * Create a websocket subscription to watch transactions of type "transfer" in the network
     *
     * @param addresses An array of address that will be added to the watch list
     * @param callback  A function to be called when the sockets has the connection ready. Has the socket as a param
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribe(final List<String> addresses, final Function callback) throws HancockException {
        return subscribe(addresses, "", callback);
    }

    /**
     * Create a websocket subscription to watch transactions of type "transfer" in the network
     *
     * @param addresses An array of address that will be added to the watch list
     * @param consumer  A consumer plugin previously configured in hancock that will handle each received event
     * @param callback  A function to be called when the sockets has the connection ready. Has the socket as a param
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribe(final List<String> addresses, final String consumer, final Function callback) throws HancockException {
        final String url = config.getBroker().getHost() + ':'
                + config.getBroker().getPort()
                + config.getBroker().getBase()
                + config.getBroker().getResources().get("events")

                .replaceAll("__ADDRESS__", "")
                .replaceAll("__SENDER__", "")
                .replaceAll("__CONSUMER__", consumer);
        try {
            final HancockSocket socket = new HancockSocket(url);
            socket.on("ready", o -> {
                socket.watchTransfer(addresses);
                if (callback != null) {
                    callback.apply(socket);
                }
                return null;
            });
            return socket;
        } catch (final URISyntaxException e) {
            LOGGER.error(e.getMessage());
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50003", 500, HancockErrorEnum.ERROR_SOCKET.getMessage(), HancockErrorEnum.ERROR_SOCKET.getMessage(), e);
        }
    }

    /**
     * Given a transaction request, fills it with all the info necessary
     *
     * @param txRequest The transaction with the minimum data to be fill (From, To, Value)
     * @return The transaction with all the data fill (Gas, GasPrice, Nonce)
     * @throws Exception
     */
    public EthereumTransaction adaptTransfer(final EthereumTransferRequest txRequest) throws Exception {
        final String url = getResourceUrl(config, "transfer");
        final Gson gson = new Gson();
        final String json = gson.toJson(txRequest);
        final RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);
        final Request request = getRequest(url, body);

        final Response response = makeCall(request);
        final EthereumTransactionAdaptResponse rawTx = checkStatus(response, EthereumTransactionAdaptResponse.class);
        if (rawTx.getData().getData() == null) {
            rawTx.getData().setData("");
        }
        return rawTx.getData();
    }
}
