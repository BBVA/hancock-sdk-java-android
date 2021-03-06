package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.HancockSocket;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumSendToProviderRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumSendTransactionRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumSignedTransactionRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.models.TransactionConfig;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static com.bbva.hancock.sdk.Common.*;

public class EthereumTransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EthereumTransactionService.class);
    private static final MediaType CONTENT_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private final HancockConfig config;

    public EthereumTransactionService(final HancockConfig config) {
        this.config = config;
    }

    /**
     * Send a transaction to Ethereum using the configuration pass as parameter
     *
     * @param rawtx    A raw transaction which will be sent to Ethereum
     * @param txConfig Options which will be used to config the transaction
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTransactionResponse send(final EthereumTransaction rawtx, final TransactionConfig txConfig) throws Exception {

        if (txConfig.getPrivateKey() != null) {
            final String signedTransaction = signTransaction(new EthereumRawTransaction(rawtx), txConfig.getPrivateKey());
            if (txConfig.getSendLocally()) {
                return sendSignedTransactionLocally(signedTransaction);
            }
            return sendSignedTransaction(signedTransaction, txConfig);
        } else if (txConfig.getProvider() != null) {
            return sendToSignProvider(rawtx, txConfig);
        }

        return sendRawTransaction(rawtx);
    }

    /**
     * Sign a raw message with the given private key
     *
     * @param message    A raw message which will be signed
     * @param privateKey The private key with which the raw message will be signed
     * @return The signed message as hex string
     */
    public String signMessage(final String message, final String privateKey) {

        final byte[] messageBytes = message.getBytes();
        final byte[] messageHash = Hash.sha3(messageBytes);
        final Credentials credentials = Credentials.create(privateKey);
        final Sign.SignatureData signatureData = Sign.signMessage(messageHash, credentials.getEcKeyPair(), false);

        final String strR = Numeric.toHexString(signatureData.getR(), 0, 32, false);
        final String strS = Numeric.toHexString(signatureData.getS(), 0, 32, false);
        final String strV = Numeric.toHexString(new byte[]{signatureData.getV()}, 0, 1, false);

        return "0x" + (strR.concat(strS).concat(strV));

    }

    /**
     * Sign a raw personal message with the given private key using
     * https://github.com/ethereum/EIPs/blob/master/EIPS/eip-712.md
     *
     * @param message    A raw message which will be signed
     * @param privateKey The private key with which the raw message will be signed
     * @return The signed message as hex string
     */
    public String signPersonalMessage(final String message, final String privateKey) {

        final String prefix = "\u0019Ethereum Signed Message:\n" + message.length();
        final String msg = prefix + message;

        return signMessage(msg, privateKey);

    }

    /**
     * Sign a raw transaction with a given private key
     *
     * @param rawTransaction A raw transaction which will be signed with the privatekey
     * @param privateKey     The private key with which the raw transaction will be signed
     * @return The signed transaction
     */
    public String signTransaction(final EthereumRawTransaction rawTransaction, final String privateKey) {

        final Credentials credentials = Credentials.create(privateKey);
        final byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction.getWeb3Instance(), credentials);
        final String hexValue = Numeric.toHexString(signedMessage);

        return hexValue;
    }

    /**
     * Send a raw transaction to the ethereum network (It is assumed that the "from" address which will sign the transaction is stored in the node)
     *
     * @param rawTx A raw transaction which will be sent to the network
     * @return The result of the transaction
     * @throws HancockException
     */
    public EthereumTransactionResponse sendRawTransaction(final EthereumTransaction rawTx) throws HancockException {
        final String url = config.getWallet().getHost() + ':' + config.getWallet().getPort() + config.getWallet().getBase() + config.getWallet().getResources().get("sendTx");
        final EthereumSendTransactionRequest sendBody = new EthereumSendTransactionRequest(rawTx);
        final Gson gson = new Gson();
        final String json = gson.toJson(sendBody);
        final RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);
        final Request request = getRequest(url, body);

        final Response response = makeCall(request);
        final EthereumTransactionResponse txResponse = checkStatus(response, EthereumTransactionResponse.class);
        return txResponse;
    }

    /**
     * Send a raw transaction to one of the sign providers registered in hancock
     *
     * @param rawTx    A raw transaction which will be signed by the sign provider
     * @param txConfig Options which will be used to config the transaction (provider and callBack Options(optional))
     * @return The result of the request
     * @throws HancockException
     */
    public EthereumTransactionResponse sendToSignProvider(final EthereumTransaction rawTx, final TransactionConfig txConfig) throws HancockException {
        final String url = config.getWallet().getHost() + ':' + config.getWallet().getPort() + config.getWallet().getBase() + config.getWallet().getResources().get("signTx");
        final EthereumSendToProviderRequest sendBody = new EthereumSendToProviderRequest(rawTx, txConfig.getProvider());
        String requestId = "";
        if (txConfig.getCallbackOptions() != null) {
            requestId = txConfig.getCallbackOptions().getRequestId();
            sendBody.setBackUrl(txConfig.getCallbackOptions().getBackUrl());
        }
        final Gson gson = new Gson();
        final String json = gson.toJson(sendBody);
        final RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("vnd-hancock-request-id", requestId)
                .build();

        final Response response = makeCall(request);
        final EthereumTransactionResponse txResponse = checkStatus(response, EthereumTransactionResponse.class);
        return txResponse;
    }

    /**
     * Send a signed transaction to the ethereum network
     *
     * @param signedTransaction A signed transaction which will be send to the network
     * @param txConfig          Options which will be used to config the transaction
     * @return The result of the transaction
     * @throws Exception
     */
    public EthereumTransactionResponse sendSignedTransaction(final String signedTransaction, final TransactionConfig txConfig) throws Exception {

        final String url = config.getWallet().getHost() + ':' + config.getWallet().getPort() + config.getWallet().getBase() + config.getWallet().getResources().get("sendSignedTx");
        final EthereumSignedTransactionRequest signedTxBody = new EthereumSignedTransactionRequest(signedTransaction);
        final Gson gson = new Gson();
        final String json = gson.toJson(signedTxBody);
        final RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);
        String requestId = "";

        if (txConfig.getCallbackOptions() != null) {
            requestId = txConfig.getCallbackOptions().getRequestId();
        }

        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("vnd-hancock-request-id", requestId)
                .build();

        final Response response = makeCall(request);
        final EthereumTransactionResponse txResponse = checkStatus(response, EthereumTransactionResponse.class);
        return txResponse;

    }

    /**
     * Send a signed transaction directly to a node of Ethereum
     *
     * @param signedTransaction A signed transaction which will be send to the network
     * @return The transaction hash
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public EthereumTransactionResponse sendSignedTransactionLocally(final String signedTransaction) throws InterruptedException, ExecutionException {

        final String port = config.getNode().getPort() >= 0 ? ":" + String.valueOf(config.getNode().getPort()) : "";

        final Web3j web3j = Web3jFactory.build(new HttpService(config.getNode().getHost() + port));


        final EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedTransaction).sendAsync().get();

        final Boolean success;

        final String transactionHash = ethSendTransaction.getTransactionHash();

        if (transactionHash != null && !transactionHash.isEmpty()) {
            success = true;
        } else {
            throw new ExecutionException(ethSendTransaction.getError().getMessage(), null);
        }

        final EthereumTransactionResponse response = new EthereumTransactionResponse(success, transactionHash);

        return response;

    }

    /**
     * Create a websocket subscription to watch transactions of type "transactions" in the network
     *
     * @param addresses An array of address that will be added to the watch list
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribe(final List<String> addresses) throws HancockException {
        return subscribe(addresses, "", null);
    }

    /**
     * Create a websocket subscription to watch transactions of type "transactions" in the network
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
     * Create a websocket subscription to watch transactions of type "transactions" in the network
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
     * Create a websocket subscription to watch transactions of type "transactions" in the network
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
                socket.watchTransaction(addresses);
                if (callback != null) {
                    callback.apply(socket);
                }
                return null;
            });
            return socket;
        } catch (final HancockException e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }
}
