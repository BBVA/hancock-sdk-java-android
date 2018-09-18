package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.HancockClient;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.TransactionConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumSendToProviderRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumSendTransactionRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumSignedTransactionRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.exception.HancockException;
import com.google.gson.Gson;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.util.concurrent.ExecutionException;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bbva.hancock.sdk.Common.checkStatus;
import static com.bbva.hancock.sdk.Common.getRequest;
import static com.bbva.hancock.sdk.Common.makeCall;

public class HancockEthereumTransactionClient extends HancockClient {

    public HancockEthereumTransactionClient(HancockConfig config) throws Exception {
        super(config);
    }

    protected EthereumTransactionResponse send(EthereumRawTransaction rawtx, TransactionConfig txConfig) throws Exception {

        if (txConfig.getPrivateKey() != null) {
            String signedTransaction = this.signTransaction(rawtx, txConfig.getPrivateKey());
            return this.sendSignedTransaction(signedTransaction, txConfig);
        } else if (txConfig.getProvider() != null){
            return this.sendToSignProvider(rawtx, txConfig);
        }

        return this.sendRawTransaction(rawtx);
    }

    public String signTransaction(EthereumRawTransaction rawTransaction, String privateKey) {

        Credentials credentials = Credentials.create(privateKey);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction.getWeb3Instance(), credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        return hexValue;
    }

    public EthereumTransactionResponse sendRawTransaction(EthereumRawTransaction rawTx) throws HancockException{
        String url = getConfig().getWallet().getHost() + ':' + getConfig().getWallet().getPort() +  getConfig().getWallet().getBase() +  getConfig().getWallet().getResources().get("sendTx");
        EthereumSendTransactionRequest sendBody = new EthereumSendTransactionRequest(rawTx);
        Gson gson = new Gson();
        String json = gson.toJson(sendBody);
        RequestBody body = RequestBody.create(getContentType(), json);
        Request request = getRequest(url, body);

        Response response = makeCall(request);
        EthereumTransactionResponse txResponse = checkStatus(response, EthereumTransactionResponse.class);
        return txResponse;
    }

    public EthereumTransactionResponse sendToSignProvider(EthereumRawTransaction rawTx, TransactionConfig txConfig) throws HancockException{
        String url = getConfig().getWallet().getHost() + ':' + getConfig().getWallet().getPort() +  getConfig().getWallet().getBase() +  getConfig().getWallet().getResources().get("signTx");
        EthereumSendToProviderRequest sendBody = new EthereumSendToProviderRequest(rawTx, txConfig.getProvider());
        Gson gson = new Gson();
        String json = gson.toJson(sendBody);
        RequestBody body = RequestBody.create(getContentType(), json);
        Request request = getRequest(url, body);

        Response response = makeCall(request);
        EthereumTransactionResponse txResponse = checkStatus(response, EthereumTransactionResponse.class);
        return txResponse;
    }

    public EthereumTransactionResponse sendSignedTransaction(String signedTransaction, TransactionConfig txConfig) throws Exception {

        String url = getConfig().getWallet().getHost() + ':' + getConfig().getWallet().getPort() +  getConfig().getWallet().getBase() +  getConfig().getWallet().getResources().get("sendSignedTx");
        EthereumSignedTransactionRequest signedTxBody = new EthereumSignedTransactionRequest(signedTransaction);
        Gson gson = new Gson();
        String json = gson.toJson(signedTxBody);
        RequestBody body = RequestBody.create(getContentType(), json);
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

    public String sendSignedTransactionLocally(String signedTransaction) throws InterruptedException, ExecutionException {

        Web3j web3j = Web3jFactory.build(new HttpService(getConfig().getNode().getHost() + ":" + getConfig().getNode().getPort()));

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedTransaction).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();

        return transactionHash;


    }
}
