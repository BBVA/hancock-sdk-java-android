package com.bbva.hancock.sdk;

import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.models.EthereumTransferResponse;
import com.bbva.hancock.sdk.models.GetBalanceResponse;
import com.bbva.hancock.sdk.models.HancockProtocolAction;
import com.bbva.hancock.sdk.models.HancockProtocolDecodeRequest;
import com.bbva.hancock.sdk.models.HancockProtocolDecodeResponse;
import com.bbva.hancock.sdk.models.HancockProtocolDlt;
import com.bbva.hancock.sdk.models.HancockProtocolEncodeRequest;
import com.bbva.hancock.sdk.models.HancockProtocolEncodeResponse;
import com.bbva.hancock.sdk.models.TransactionConfig;
import com.bbva.hancock.sdk.models.EthereumTransferRequest;
import com.google.gson.Gson;
import okhttp3.*;
import org.web3j.crypto.*;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;


/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class HancockEthereumClient {

    private static final MediaType CONTENT_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private HancockConfig config;

    public HancockEthereumClient() {

        this.config = new HancockConfig
                .Builder()
                .build();

    }

    public HancockEthereumClient(HancockConfig config) throws Exception {

        this.config = config;
    }

    public EthereumWallet generateWallet() throws Exception {

        try {

            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            Credentials credentials = Credentials.create(ecKeyPair);

            String address = credentials.getAddress();
            String privateKey = "0x" + credentials.getEcKeyPair().getPrivateKey().toString(16);
            String publicKey = "0x" + credentials.getEcKeyPair().getPublicKey().toString(16);

            return new EthereumWallet(address, privateKey, publicKey);

        } catch (Exception error) {

            System.out.println("error: " + error.toString());
            throw new Exception("Error generating wallet");

        }

    }

    public BigInteger getBalance(String address) throws IOException {
        
        String url = this.config.getAdapter().getHost() + ':' + this.config.getAdapter().getPort() + this.config.getAdapter().getBase() + this.config.getAdapter().getResources().get("balance").replaceAll("__ADDRESS__", address);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = makeCall(request);
        GetBalanceResponse responseModel = checkStatus(response, GetBalanceResponse.class);
        return new BigInteger(responseModel.getBalance());

    }

    protected <T> T checkStatus(Response response, Class<T> tClass) throws IOException {

        try (ResponseBody responseBody = response.body()) {

            // HTTP status code between 200 and 299
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


            Gson gson = new Gson();
            return gson.fromJson(responseBody.string(), tClass);

        }

    }

    public String signTransaction(EthereumRawTransaction rawTransaction, String privateKey) {

        Credentials credentials = Credentials.create(privateKey);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction.getWeb3Instance(), credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        return hexValue;
    }

    public String sendSignedTransaction(String signedTransaction, boolean locally) throws Exception {

        String url = locally
                ? (this.config.getNode().getHost() + ':' + this.config.getNode().getPort())
                : ("");

        return sendSignedTransaction(signedTransaction, locally, url);

    }

    public String sendSignedTransaction(String signedTransaction, boolean locally, String url) throws Exception {

        if (locally) {
            return this.sendSignedTransactionLocally(signedTransaction, url);
        } else {
            return this.sendSignedTransactionRemotely(signedTransaction, url);
        }

    }

    private String sendSignedTransactionLocally(String signedTransaction) throws InterruptedException, ExecutionException {
        String nodeUrl = this.config.getNode().getHost() + ':' + this.config.getNode().getPort();
        return this.sendSignedTransactionLocally(signedTransaction, nodeUrl);
    }

    private String sendSignedTransactionLocally(String signedTransaction, String nodeUrl) throws InterruptedException, ExecutionException {

        // defaults to http://localhost:8545/
        Web3j web3j = Web3jFactory.build(new HttpService(nodeUrl));

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedTransaction).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();
        // poll for transaction response via org.web3j.protocol.Web3j.ethGetTransactionReceipt(<txHash>)

        return transactionHash;

    }

    private String sendSignedTransactionRemotely(String signedTransaction, String backUrl) throws Exception {

       throw new Exception("Not implemented");

    }

    public String transfer(EthereumTransferRequest request, TransactionConfig txConfig) throws Exception{
        EthereumRawTransaction rawtx = this.adaptTransfer(request);

        String requestUrl = "";
        String signedTransaction = "";

        if (txConfig.getSendLocally()) {
            requestUrl = txConfig.getNode() != null ? txConfig.getNode() : this.config.getNode().getHost() + ':' + this.config.getNode().getPort();
        } else {
            //TODO with hancock
        }

        if (txConfig.getPrivateKey() != null) {
            signedTransaction = this.signTransaction(rawtx, txConfig.getPrivateKey());
        } else {
            //TODO with provider
        }

        return this.sendSignedTransaction(signedTransaction, txConfig.getSendLocally(), requestUrl);
    }

    public EthereumRawTransaction adaptTransfer(EthereumTransferRequest txRequest) throws Exception {
        
        String url = getResourceUrl("transfer");

        Gson gson = new Gson();
        String json = gson.toJson(txRequest);
        RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);
        Request request = getRequest(url, body);

        Response response = makeCall(request);
        EthereumTransferResponse rawTx = checkStatus(response, EthereumTransferResponse.class);
        return new EthereumRawTransaction(rawTx.getNonce(), rawTx.getGasPrice(), rawTx.getGas(), rawTx.getTo(), rawTx.getValue(), rawTx.getData());
    }

    public HancockProtocolDecodeResponse decodeProtocol(String code) throws IOException {
        
        String url = getResourceUrl("decode");

        Gson gson = new Gson();
        HancockProtocolDecodeRequest hancockRequest = new HancockProtocolDecodeRequest(code);
        String json = gson.toJson(hancockRequest);
        RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);

        Request request = getRequest(url, body);

        Response response = makeCall(request);
        HancockProtocolDecodeResponse responseModel = checkStatus(response, HancockProtocolDecodeResponse.class);

        return responseModel;
    }

    public HancockProtocolEncodeResponse encodeProtocol(HancockProtocolAction action, BigInteger value, String to, String data, HancockProtocolDlt dlt) throws IOException {
       
        String url = getResourceUrl("encode");

        Gson gson = new Gson();
        HancockProtocolEncodeRequest hancockRequest = new HancockProtocolEncodeRequest(action, value, to, data, dlt);
        String json = gson.toJson(hancockRequest);
        RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);

        Request request = getRequest(url, body);

        Response response = makeCall(request);
        HancockProtocolEncodeResponse responseModel = checkStatus(response, HancockProtocolEncodeResponse.class);

        return responseModel;
    }

    protected String getResourceUrl(String encode) {
        return this.config.getAdapter().getHost() + ':' +
                this.config.getAdapter().getPort() +
                this.config.getAdapter().getBase() +
                this.config.getAdapter().getResources().get(encode);
    }

    protected Request getRequest(String url, RequestBody body) {
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }
    
    protected Response makeCall(Request request) throws IOException {
      OkHttpClient httpClient = new OkHttpClient();
      Response response = httpClient.newCall(request).execute();
      return response;
    }

    // TODO: Support yaml load config on android
    /*private HancockConfig loadPrivateConfig() {
        Config baseConfig = ConfigFactory.load();
        Config config, specificConfig;

        try {

            specificConfig = ConfigFactory.load(System.getenv("BUILD_ENV"));
            config = baseConfig.withFallback(specificConfig);

        } catch (Exception e) {

            config = baseConfig;

        }

        HancockConfig hancockConfig = ConfigBeanFactory.create(config, HancockConfig.class);
        return hancockConfig;
    }*/


}