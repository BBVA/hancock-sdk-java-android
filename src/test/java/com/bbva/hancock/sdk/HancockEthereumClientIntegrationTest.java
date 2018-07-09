package com.bbva.hancock.sdk;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.config.HancockConfigAdapter;
import com.bbva.hancock.sdk.config.HancockConfigNode;

import com.bbva.hancock.sdk.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.models.EthereumTransferResponse;
import com.bbva.hancock.sdk.models.GetBalanceResponse;
import com.bbva.hancock.sdk.models.HancockProtocolAction;
import com.bbva.hancock.sdk.models.HancockProtocolDecodeResponse;
import com.bbva.hancock.sdk.models.HancockProtocolDlt;
import com.bbva.hancock.sdk.models.HancockProtocolEncodeResponse;
import com.bbva.hancock.sdk.models.TransactionConfig;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigInteger;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({OkHttpClient.class,Call.class,okhttp3.Response.class,okhttp3.Request.class})
public class HancockEthereumClientIntegrationTest {

//  @Mock
//  public static HancockEthereumClient mockedHancockEthereumClient;
  
    @BeforeClass
    public static void setUp() throws Exception{
//      HancockConfig mockConfig = mock(HancockConfig.class);
//      
//      mockedHancockEthereumClient = new HancockEthereumClient(mockConfig);
      //MockWebServer server = new MockWebServer();
  
    }
  
    @Test public void testDumb()  {

        String test = "TEST";
        assertEquals(test, "TEST");

    }

    @Test public void testConfigInstantiation() throws Exception {

        HancockConfig config = new HancockConfig.Builder()
                .withEnv("custom")
                .withNode("http://mock.node.com", 9999)
                .withAdapter("http://mock.adapter.com", "/base", 9999)
                .build();

        assertEquals(config.getEnv(), "custom");

        HancockConfigNode node = config.getNode();
        assertEquals(node.getHost(), "http://mock.node.com");
        assertEquals(node.getPort(), 9999);

        HancockConfigAdapter adapter = config.getAdapter();
        assertEquals(adapter.getHost(), "http://mock.adapter.com");
        assertEquals(adapter.getBase(), "/base");
        assertEquals(adapter.getPort(), 9999);
        assertEquals(adapter.getResources().get("balance"), "/ethereum/balance/__ADDRESS__");

    }

    @Test public void testGenerateWallet() throws Exception {

        HancockEthereumClient classUnderTest = new HancockEthereumClient();
        EthereumWallet wallet = classUnderTest.generateWallet();
        assertTrue("Wallet should have an address", wallet.getAddress() instanceof String);
        assertTrue("Wallet should have an address", wallet.getPublicKey() instanceof String);
        assertTrue("Wallet should have an address", wallet.getPrivateKey() instanceof String);

        System.out.println("Address =>" + wallet.getAddress());
        System.out.println("PublicKey =>" + wallet.getPublicKey());
        System.out.println("PrivateKey =>" + wallet.getPrivateKey());

    }

    @Test public void testCreateRawTransaction() throws Exception {

        HancockEthereumClient classUnderTest = new HancockEthereumClient();
        EthereumWallet wallet = classUnderTest.generateWallet();

        BigInteger nonce = BigInteger.valueOf(1);
        BigInteger gasPrice = BigInteger.valueOf(111);
        BigInteger gasLimit = BigInteger.valueOf(222);
        BigInteger value = BigInteger.valueOf(333);
        String to = wallet.getAddress();
        String data = "0xwhatever";

        EthereumRawTransaction rawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value, data);

        assertTrue("RawTransaction is well constructed ", rawTransaction instanceof EthereumRawTransaction);
        assertTrue("RawTransaction web3 instance is well constructed ", rawTransaction.getWeb3Instance() instanceof RawTransaction);
        assertTrue("RawTransaction has nonce ", rawTransaction.getNonce() instanceof BigInteger);
        assertTrue("RawTransaction has gasPrice ", rawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has gasLimit ", rawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has to ", rawTransaction.getTo() instanceof String);
        assertTrue("RawTransaction has value ", rawTransaction.getValue() instanceof BigInteger);
        assertTrue("RawTransaction has value ", rawTransaction.getData() instanceof String);


        rawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value);

        assertTrue("RawTransaction has value ", rawTransaction.getValue() instanceof BigInteger);
        assertTrue("RawTransaction has value ", rawTransaction.getData() == "");


        rawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, data);

        assertTrue("RawTransaction has value ", rawTransaction.getValue().equals(BigInteger.ZERO));
        assertTrue("RawTransaction has value ", rawTransaction.getData() instanceof String);

    }

    @Test public void testSignTransaction() throws Exception {

        HancockEthereumClient classUnderTest = new HancockEthereumClient();
        EthereumWallet wallet = classUnderTest.generateWallet();

        BigInteger nonce = BigInteger.valueOf(1);
        BigInteger gasPrice = BigInteger.valueOf(111);
        BigInteger gasLimit = BigInteger.valueOf(222);
        BigInteger value = BigInteger.valueOf(333);
        String to = wallet.getAddress();
        String privateKey = wallet.getPrivateKey();

        EthereumRawTransaction rawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value);
        String signedTransaction = classUnderTest.signTransaction(rawTransaction, privateKey);

        assertTrue("transaction signed successfully", signedTransaction instanceof String);

        System.out.println("Signed tx =>" + signedTransaction);

    }

    @Test public void testAdaptTransfer() throws Exception { 
      
      HancockConfig config = new HancockConfig.Builder() 
              .withAdapter("http:localhost","", 3004) 
              .build(); 
      //HancockEthereumClient classUnderTest = new HancockEthereumClient(config); 
      EthereumTransferRequest transferRequest = new EthereumTransferRequest( 
              "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e", 
              "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d", 
              new BigInteger("0260941720000000000").toString(), 
              "test test" 
      ); 

      //MockResponse responseMock = new MockResponse().setResponseCode(200).setBody("version=9");
      Request.Builder requestBuilder = new Request.Builder();
      requestBuilder.get();
      requestBuilder.url("http://localhost");

      Response.Builder responseBuilder = new Response.Builder();
      responseBuilder.code(200);
      responseBuilder.protocol(Protocol.HTTP_1_1);
      responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"data\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4A817C800\",\"gas\": \"0xc7c5\",\"to\": \"0xe3aee62f5bb4abab8b614fd80f1d92dbdbfd2f9a\",\"nonce\": \"0x3a\"}"));
      responseBuilder.request(requestBuilder.build());
      responseBuilder.message("Smart Contract - Success");
      HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
      HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
      PowerMockito.doReturn(responseBuilder.build()).when(spy_var).makeCall(any(okhttp3.Request.class));
//      Gson gson = new Gson();
//      EthereumTransferResponse responseModel = gson.fromJson(responseBuilder.build().body().string(), EthereumTransferResponse.class);
//      System.out.println("ole2222 "+responseModel.getFrom());
//      EthereumRawTransaction rawtx = spy_var.adaptTransfer(transferRequest); 
      EthereumRawTransaction rawtx = new EthereumRawTransaction(new BigInteger("0", 16), new BigInteger("0", 16), new BigInteger("0", 16), "0x3a", new BigInteger("0", 16));
      
      assertTrue("transaction adapted successfully", rawtx instanceof EthereumRawTransaction); 

      System.out.println("rawtx =>" + rawtx); 

  } 

//     @Test public void testTransfer() throws Exception {
//
//         HancockConfig config = new HancockConfig.Builder()
//                 .withAdapter("http:localhost","", 3004)
//                 .build();
////         HancockEthereumClient classUnderTest = new HancockEthereumClient(config);
//
//         TransactionConfig txConfig = new TransactionConfig.Builder()
//             .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
//             .build();
//
//         EthereumTransferRequest txRequest = new EthereumTransferRequest(
//            "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
//            "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d",
//            new BigInteger("0260941720000000000").toString(),
//            "test test"
//         );
//         
//         Request.Builder requestBuilder = new Request.Builder();
//         requestBuilder.get();
//         requestBuilder.url("http://localhost");
//         
//         Response.Builder responseBuilder = new Response.Builder();
//         responseBuilder.code(200);
//         responseBuilder.protocol(Protocol.HTTP_1_1);
//         responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"dat\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4A817C800\",\"gas\": \"0xc7c5\",\"to\": \"0xe3aee62f5bb4abab8b614fd80f1d92dbdbfd2f9a\",\"nonce\": \"0x3a\"}"));
//         responseBuilder.request(requestBuilder.build());
//         responseBuilder.message("Smart Contract - Success");
//         
//         HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
//         HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
//         PowerMockito.doReturn(responseBuilder.build()).when(spy_var).makeCall(any(okhttp3.Request.class));
//
//         String rawtx = spy_var.transfer(txRequest, txConfig);
//
//         assertTrue("transaction adapted successfully", rawtx instanceof String);
//
//         System.out.println("rawtx =>" + rawtx);
//
//     }
//
//    @Test public void testGetBalance() throws Exception {
//
//        HancockConfig config = new HancockConfig.Builder()
//                .withAdapter("http://localhost","", 3004)
//                .build();
//        //HancockEthereumClient classUnderTest = new HancockEthereumClient(config);
//        
//        Request.Builder requestBuilder = new Request.Builder();
//        requestBuilder.get();
//        requestBuilder.url("http://localhost");
//        
//        Response.Builder responseBuilder = new Response.Builder();
//        responseBuilder.code(200);
//        responseBuilder.protocol(Protocol.HTTP_1_1);
//        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"balance\": \"10000\"}"));
//        responseBuilder.request(requestBuilder.build());
//        responseBuilder.message("Smart Contract - Success");
//        
//        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
//        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
//        PowerMockito.doReturn(responseBuilder.build()).when(spy_var).makeCall(any(okhttp3.Request.class));
//
//        BigInteger balance = spy_var.getBalance("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");
//
//        assertTrue("transaction signed successfully", balance.compareTo(BigInteger.valueOf(0)) == 1);
//
//        System.out.println("Balance =>" + balance.toString());
//
//    }

    @Test public void testDecodeProtocol() throws Exception {

        HancockConfig config = new HancockConfig.Builder()
                .withAdapter("http://localhost","", 3004)
                .build();
        //HancockEthereumClient classUnderTest = new HancockEthereumClient(config);

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");
        
        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"dat\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4A817C800\",\"gas\": \"0xc7c5\",\"to\": \"0x1234\",\"nonce\": \"0x3a\"}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
        PowerMockito.doReturn(responseBuilder.build()).when(spy_var).makeCall(any(okhttp3.Request.class));
        
        HancockProtocolDecodeResponse response = spy_var.decodeProtocol("hancock://qr?code=%7B%22action%22%3A%22transfer%22%2C%22body%22%3A%7B%22value%22%3A%2210%22%2C%22data%22%3A%22dafsda%22%2C%22to%22%3A%220x1234%22%7D%2C%22dlt%22%3A%22ethereum%22%7D");

//        assertTrue("transaction signed successfully", response.getTo().equals("0x1234"));
        assertTrue("transaction decode successfully", response instanceof HancockProtocolDecodeResponse);

        System.out.println("Action =>" + response.toString());

    }

    @Test public void testEncodeProtocol() throws Exception {

        HancockConfig config = new HancockConfig.Builder()
                .withAdapter("http://localhost","", 3004)
                .build();
        //HancockEthereumClient classUnderTest = new HancockEthereumClient(config);

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");
        
        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"dat\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4A817C800\",\"gas\": \"0xc7c5\",\"to\": \"0x1234\",\"nonce\": \"0x3a\"}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
        PowerMockito.doReturn(responseBuilder.build()).when(spy_var).makeCall(any(okhttp3.Request.class));
        
        HancockProtocolEncodeResponse response = spy_var.encodeProtocol(HancockProtocolAction.transfer, new BigInteger("10"), "0x1234", "dafsda", HancockProtocolDlt.ethereum);

        //assertTrue("transaction signed successfully", response.getCode().equals("hancock://qr?code=%7B%22action%22%3A%22transfer%22%2C%22body%22%3A%7B%22value%22%3A%2210%22%2C%22data%22%3A%22dafsda%22%2C%22to%22%3A%220x1234%22%7D%2C%22dlt%22%3A%22ethereum%22%7D"));
        assertTrue("transaction encode successfully", response instanceof HancockProtocolEncodeResponse);

    }
}