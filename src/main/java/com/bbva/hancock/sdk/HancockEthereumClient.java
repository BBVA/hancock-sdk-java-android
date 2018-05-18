package com.bbva.hancock.sdk;

import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.models.GetBalanceResponse;
import com.bbva.hancock.sdk.models.TransferResponse;
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

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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

        OkHttpClient httpClient = new OkHttpClient();
        String url = this.config.getAdapter().getHost() + ':' + this.config.getAdapter().getPort() + this.config.getAdapter().getBase() + this.config.getAdapter().getResources().get("balance").replaceAll("__ADDRESS__", address);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = httpClient.newCall(request).execute();
        GetBalanceResponse responseModel = checkStatus(response, GetBalanceResponse.class);
        return new BigInteger(responseModel.getBalance());

    }

    private <T> T checkStatus(Response response, Class<T> tClass) throws IOException {

        try (ResponseBody responseBody = response.body()) {

            // HTTP status code between 200 and 299
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


            Gson gson = new Gson();
            return gson.fromJson(responseBody.string(), tClass);

        }

    }

    public EthereumRawTransaction createRawTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, String data) {
        return new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, data);
    }

    public EthereumRawTransaction createRawTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value) {
        return new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value);
    }

    public EthereumRawTransaction createRawTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data) {
        return new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value, data);
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

    public String transfer(String from, String to, String value, String data, String privateKey) throws Exception{
        EthereumRawTransaction rawtx = this.adaptTransfer(from, to, value, data);
        String signedTransaction = this.signTransaction(rawtx, privateKey);

        String nodeurl = this.config.getNode().getHost() + ':' + this.config.getNode().getPort();
        return this.sendSignedTransaction(signedTransaction, true, nodeurl);
    }

    private EthereumRawTransaction adaptTransfer(String from, String to, String value, String data) throws Exception {
        OkHttpClient httpClient = new OkHttpClient();
        String url = this.config.getAdapter().getHost() + ':' + this.config.getAdapter().getPort() + this.config.getAdapter().getBase() + this.config.getAdapter().getResources().get("transfer");
        
        String json = "{\"from\":\""+from+"\", \"to\":\""+to+"\", \"value\":\""+value+"\", \"data\":\""+data+"\"}";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = httpClient.newCall(request).execute();
        TransferResponse rawTx = checkStatus(response, TransferResponse.class);
        EthereumRawTransaction ethrawtx = this.createRawTransaction(rawTx.getNonce(), rawTx.getGasPrice(), rawTx.getGas(), rawTx.getTo(), rawTx.getValue(), rawTx.getData());
        return ethrawtx;
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