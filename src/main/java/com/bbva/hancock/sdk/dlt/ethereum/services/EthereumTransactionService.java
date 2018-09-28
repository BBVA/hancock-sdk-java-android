package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.HancockSocket;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.*;
import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.exception.HancockTypeErrorEnum;
import com.bbva.hancock.sdk.models.TransactionConfig;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.bbva.hancock.sdk.Common.*;

public class EthereumTransactionService {

    private static final MediaType CONTENT_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private HancockConfig config;

    public EthereumTransactionService(HancockConfig config) {
        this.config = config;
    }

    /**
     * Send a transaction to Ethereum using the configuration pass as parameter
     * @param rawtx A raw transaction which will be sent to Ethereum
     * @param txConfig Options which will be used to config the transaction
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTransactionResponse send(EthereumTransaction rawtx, TransactionConfig txConfig) throws Exception {

        if (txConfig.getPrivateKey() != null) {
            String signedTransaction = this.signTransaction(new EthereumRawTransaction(rawtx), txConfig.getPrivateKey());
            return this.sendSignedTransaction(signedTransaction, txConfig);
        } else if (txConfig.getProvider() != null){
            return this.sendToSignProvider(rawtx, txConfig);
        }

        return this.sendRawTransaction(rawtx);
    }

    /**
     * Sign a raw transaction with a given private key
     * @param rawTransaction A raw transaction which will be signed with the privatekey
     * @param privateKey The private key with which the raw transaction will be signed
     * @return The signed transaction
     */
    public String signTransaction(EthereumRawTransaction rawTransaction, String privateKey) {

        Credentials credentials = Credentials.create(privateKey);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction.getWeb3Instance(), credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        return hexValue;
    }

    /**
     * Send a raw transaction to the ethereum network (It is assumed that the "from" address which will sign the transaction is stored in the node)
     * @param rawTx A raw transaction which will be sent to the network
     * @return The result of the transaction
     * @throws HancockException
     */
    public EthereumTransactionResponse sendRawTransaction(EthereumTransaction rawTx) throws HancockException{
        String url = this.config.getWallet().getHost() + ':' + this.config.getWallet().getPort() +  this.config.getWallet().getBase() +  this.config.getWallet().getResources().get("sendTx");
        EthereumSendTransactionRequest sendBody = new EthereumSendTransactionRequest(rawTx);
        Gson gson = new Gson();
        String json = gson.toJson(sendBody);
        RequestBody body = RequestBody.create(this.CONTENT_TYPE_JSON, json);
        Request request = getRequest(url, body);

        Response response = makeCall(request);
        EthereumTransactionResponse txResponse = checkStatus(response, EthereumTransactionResponse.class);
        return txResponse;
    }

    /**
     * Send a raw transaction to one of the sign providers registered in hancock
     * @param rawTx A raw transaction which will be signed by the sign provider
     * @param txConfig Options which will be used to config the transaction (provider and callBack Options(optional))
     * @return The result of the request
     * @throws HancockException
     */
    public EthereumTransactionResponse sendToSignProvider(EthereumTransaction rawTx, TransactionConfig txConfig) throws HancockException{
        String url = this.config.getWallet().getHost() + ':' + this.config.getWallet().getPort() +  this.config.getWallet().getBase() +  this.config.getWallet().getResources().get("signTx");
        EthereumSendToProviderRequest sendBody = new EthereumSendToProviderRequest(rawTx, txConfig.getProvider());
        String requestId = "";
        if (txConfig.getCallbackOptions() != null){
            requestId = txConfig.getCallbackOptions().getRequestId();
            sendBody.setBackUrl(txConfig.getCallbackOptions().getBackUrl());
        }
        Gson gson = new Gson();
        String json = gson.toJson(sendBody);
        RequestBody body = RequestBody.create(this.CONTENT_TYPE_JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("vnd-hancock-request-id", requestId)
                .build();

        Response response = makeCall(request);
        EthereumTransactionResponse txResponse = checkStatus(response, EthereumTransactionResponse.class);
        return txResponse;
    }

    /**
     * Send a signed transaction to the ethereum network
     * @param signedTransaction A signed transaction which will be send to the network
     * @param txConfig Options which will be used to config the transaction
     * @return The result of the transaction
     * @throws Exception
     */
    public EthereumTransactionResponse sendSignedTransaction(String signedTransaction, TransactionConfig txConfig) throws Exception {

        String url = this.config.getWallet().getHost() + ':' + this.config.getWallet().getPort() +  this.config.getWallet().getBase() +  this.config.getWallet().getResources().get("sendSignedTx");
        EthereumSignedTransactionRequest signedTxBody = new EthereumSignedTransactionRequest(signedTransaction);
        Gson gson = new Gson();
        String json = gson.toJson(signedTxBody);
        RequestBody body = RequestBody.create(this.CONTENT_TYPE_JSON, json);
        String requestId = "";
        if (txConfig.getCallbackOptions() != null)
            requestId = txConfig.getCallbackOptions().getRequestId();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("vnd-hancock-request-id", requestId)
                .build();

        Response response = makeCall(request);
        EthereumTransactionResponse txResponse = checkStatus(response, EthereumTransactionResponse.class);
        return txResponse;

    }

    /**
     * Send a signed transaction directly to a node of Ethereum
     * @param signedTransaction A signed transaction which will be send to the network
     * @return The transaction hash
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public String sendSignedTransactionLocally(String signedTransaction) throws InterruptedException, ExecutionException {

        Web3j web3j = Web3jFactory.build(new HttpService(this.config.getNode().getHost() + ":" + this.config.getNode().getPort()));

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedTransaction).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();

        return transactionHash;


    }

    /**
     * Create a websocket subscription to watch transactions of type "transactions" in the network
     * @param addresses An array of address that will be added to the watch list
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribe(ArrayList<String> addresses) throws HancockException{
        return this.subscribe(addresses, "");
    }

    /**
     * Create a websocket subscription to watch transactions of type "transactions" in the network
     * @param addresses An array of address that will be added to the watch list
     * @param consumer A consumer plugin previously configured in hancock that will handle each received event
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribe(ArrayList<String> addresses, String consumer) throws HancockException{
        String url = this.config.getBroker().getHost() + ':'
                + this.config.getBroker().getPort()
                + this.config.getBroker().getBase()
                + this.config.getBroker().getResources().get("events")
                .replaceAll("__ADDRESS__", "")
                .replaceAll("__SENDER__", "")
                .replaceAll("__CONSUMER__", consumer);
        try {
            HancockSocket socket = new HancockSocket(url);
            socket.on("ready", o -> {
                socket.addTransaction(addresses);
                return null;
            });
            return socket;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50003", 500, HancockErrorEnum.ERROR_SOCKET.getMessage() , HancockErrorEnum.ERROR_SOCKET.getMessage(), e);
        }
    }
}
