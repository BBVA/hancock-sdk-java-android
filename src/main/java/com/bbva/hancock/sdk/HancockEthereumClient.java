package com.bbva.hancock.sdk;

import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.models.*;
import com.bbva.hancock.sdk.models.token.allowance.EthereumTokenAllowanceRequest;
import com.bbva.hancock.sdk.models.token.metadata.GetTokenMetadataResponse;
import com.bbva.hancock.sdk.models.token.metadata.GetTokenMetadataResponseData;
import com.bbva.hancock.sdk.models.token.transfer.EthereumTokenTransferRequest;
import com.bbva.hancock.sdk.models.token.approve.EthereumTokenApproveRequest;
import com.bbva.hancock.sdk.models.token.transferFrom.EthereumTokenTransferFromRequest;

import com.google.gson.Gson;
import okhttp3.*;
import okhttp3.internal.http2.ErrorCode;

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

    public EthereumWallet generateWallet() throws HancockException {

        try {

            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            Credentials credentials = Credentials.create(ecKeyPair);

            String address = credentials.getAddress();
            String privateKey = "0x" + credentials.getEcKeyPair().getPrivateKey().toString(16);
            String publicKey = "0x" + credentials.getEcKeyPair().getPublicKey().toString(16);

            return new EthereumWallet(address, privateKey, publicKey);

        }catch (Exception error) {

            System.out.println("Wallet error: " + error.toString());
            throw new HancockException(HancockErrorEnum.ERROR_WALLET.getSystem() , HancockErrorEnum.ERROR_WALLET.getSystem(), "SDKINT_001", error, ErrorCode.INTERNAL_ERROR);

        }

    }

    public BigInteger getBalance(String address) throws HancockException {

        String url = this.config.getAdapter().getHost() + ':' + this.config.getAdapter().getPort() + this.config.getAdapter().getBase() + this.config.getAdapter().getResources().get("balance").replaceAll("__ADDRESS__", address);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = makeCall(request);
        GetBalanceResponse responseModel = checkStatus(response, GetBalanceResponse.class);
        return new BigInteger(responseModel.getBalance());

    }

    public TokenBalanceResponse getTokenBalance(String addressOrAlias, String address) throws HancockException {

      String url = this.config.getAdapter().getHost() + ':' + this.config.getAdapter().getPort() + this.config.getAdapter().getBase() + this.config.getAdapter().getResources().get("tokenBalance").replaceAll("__ADDRESS__", address).replaceAll("__ADDRESS_OR_ALIAS__", addressOrAlias);

      Request request = new Request.Builder()
              .url(url)
              .build();

      Response response = makeCall(request);
      GetTokenBalanceResponse responseModel = checkStatus(response, GetTokenBalanceResponse.class);
      return responseModel.getTokenBalance();

    }

    public GetTokenMetadataResponseData getTokenMetadata(String addressOrAlias) throws HancockException {

        String url = this.config.getAdapter().getHost() + ':' + this.config.getAdapter().getPort() + this.config.getAdapter().getBase() + this.config.getAdapter().getResources().get("tokenMetadata").replaceAll("__ADDRESS_OR_ALIAS__", addressOrAlias);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = makeCall(request);
        GetTokenMetadataResponse responseModel = checkStatus(response, GetTokenMetadataResponse.class);
        return responseModel.getTokenMetadata();

    }
    
    protected <T> T checkStatus(Response response, Class<T> tClass) throws HancockException {

        try (ResponseBody responseBody = response.body()) {

            // HTTP status code between 200 and 299
            if (!response.isSuccessful()) 
              throw new HancockException(response.message() , response.body().toString(), "SDKAPI_"+response.code(),ErrorCode.CONNECT_ERROR);

            Gson gson = new Gson();
            return gson.fromJson(responseBody.string(), tClass);

        }
        catch (IOException error) {

          System.out.println("Gson error: " + error.toString());
          throw new HancockException(error.getMessage() , error.getLocalizedMessage(), "SDKINT_002", error, ErrorCode.CONNECT_ERROR);

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

    protected String sendSignedTransactionLocally(String signedTransaction) throws InterruptedException, ExecutionException {
        String nodeUrl = this.config.getNode().getHost() + ':' + this.config.getNode().getPort();
        return this.sendSignedTransactionLocally(signedTransaction, nodeUrl);
    }

    protected String sendSignedTransactionLocally(String signedTransaction, String nodeUrl) throws InterruptedException, ExecutionException {

        // defaults to http://localhost:8545/
        Web3j web3j = Web3jFactory.build(new HttpService(nodeUrl));

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedTransaction).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();
        // poll for transaction response via org.web3j.protocol.Web3j.ethGetTransactionReceipt(<txHash>)

        return transactionHash;

    }

    protected String sendSignedTransactionRemotely(String signedTransaction, String backUrl) throws Exception {

        throw new Exception("Not implemented");

    }

    public String transfer(EthereumTransferRequest request, TransactionConfig txConfig) throws Exception{
        EthereumRawTransaction rawtx = this.adaptTransfer(request);
        return sendTransfer(txConfig, rawtx);
    }

    public String tokenTransfer(EthereumTokenTransferRequest request, TransactionConfig txConfig) throws Exception{
        EthereumRawTransaction rawtx = this.adaptTransfer(request);
        return sendTransfer(txConfig, rawtx);
    }

    public String tokenTransferFrom(EthereumTokenTransferFromRequest request, TransactionConfig txConfig) throws Exception{
        EthereumRawTransaction rawtx = this.adaptTransfer(request);
        return sendTransfer(txConfig, rawtx);
    }

    public String tokenAllowance(EthereumTokenAllowanceRequest request, TransactionConfig txConfig) throws Exception{
        EthereumRawTransaction rawtx = this.adaptTransfer(request);
        return sendTransfer(txConfig, rawtx);
    }

    public String tokenApprove(EthereumTokenApproveRequest request, TransactionConfig txConfig) throws Exception{
      EthereumRawTransaction rawtx = this.adaptTransfer(request);
      return sendTransfer(txConfig, rawtx);
    }
    
    public EthereumRawTransaction adaptTransfer(EthereumTransferRequest txRequest) throws Exception {
        String url = getTransferUrl(txRequest);
        Gson gson = new Gson();
        String json = gson.toJson(txRequest);
        RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);
        Request request = getRequest(url, body);

        Response response = makeCall(request);
        EthereumTransactionResponse rawTx = checkStatus(response, EthereumTransactionResponse.class);
        return new EthereumRawTransaction(rawTx.getNonce(), rawTx.getGasPrice(), rawTx.getGas(), rawTx.getTo(), rawTx.getValue(), rawTx.getData());
    }

    protected String getTransferUrl(EthereumTransferRequest txRequest){
        String url;

        if (txRequest instanceof EthereumTokenTransferFromRequest) {

            url = getResourceUrl("tokenTransferFrom").replaceAll("__ADDRESS_OR_ALIAS__", ((EthereumTokenTransferFromRequest) txRequest).getAddressOrAlias());

        } else if (txRequest instanceof EthereumTokenTransferRequest) {

            url = getResourceUrl("tokenTransfer").replaceAll("__ADDRESS_OR_ALIAS__", ((EthereumTokenTransferRequest) txRequest).getAddressOrAlias());

        } else if (txRequest instanceof EthereumTokenAllowanceRequest) {

            url = getResourceUrl("tokenAllowance").replaceAll("__ADDRESS_OR_ALIAS__", ((EthereumTokenAllowanceRequest) txRequest).getAddressOrAlias());
        }else if(txRequest instanceof EthereumTokenApproveRequest){
          
            url = getResourceUrl("tokenApprove").replaceAll("__ADDRESS_OR_ALIAS__", ((EthereumTokenApproveRequest) txRequest).getAddressOrAlias());    
        }else{
            url = getResourceUrl("transfer");

        }

        return url;
    }

    protected String sendTransfer(TransactionConfig txConfig, EthereumRawTransaction rawtx) throws Exception {
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

    public HancockProtocolDecodeResponse decodeProtocol(String code) throws HancockException {

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

    public HancockProtocolEncodeResponse encodeProtocol(HancockProtocolAction action, BigInteger value, String to, String data, HancockProtocolDlt dlt) throws HancockException {

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

    public HancockTokenRegisterResponse tokenRegister(String alias, String address) throws Exception {

        String url = getResourceUrl("tokenRegister");

        Gson gson = new Gson();
        HancockTokenRegisterRequest hancockRequest = new HancockTokenRegisterRequest(alias, address);
        String json = gson.toJson(hancockRequest);
        RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);

        Request request = getRequest(url, body);

        Response response = makeCall(request);
        return checkStatus(response, HancockTokenRegisterResponse.class);
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

    protected Response makeCall(Request request) throws HancockException {
      
      try{
        OkHttpClient httpClient = new OkHttpClient();
        Response response = httpClient.newCall(request).execute();
        return response;
      } catch (Exception error) {

        System.out.println("Hancock error: " + error.toString() +" /// "+ error.getMessage()+" /// "+error.getLocalizedMessage());
        throw new HancockException(error.getMessage() , error.getLocalizedMessage(), "SDKINT_003", error, ErrorCode.CONNECT_ERROR);

      }
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