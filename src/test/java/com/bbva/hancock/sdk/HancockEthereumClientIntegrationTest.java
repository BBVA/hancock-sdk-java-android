package com.bbva.hancock.sdk;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.config.HancockConfigAdapter;
import com.bbva.hancock.sdk.config.HancockConfigNode;

import com.bbva.hancock.sdk.models.*;
import com.bbva.hancock.sdk.models.token.allowance.EthereumTokenAllowanceRequest;
import com.bbva.hancock.sdk.models.token.metadata.GetTokenMetadataResponse;
import com.bbva.hancock.sdk.models.token.metadata.GetTokenMetadataResponseData;
import com.bbva.hancock.sdk.models.token.transfer.EthereumTokenTransferRequest;
import com.bbva.hancock.sdk.models.token.approve.EthereumTokenApproveRequest;

import com.bbva.hancock.sdk.models.token.transferFrom.EthereumTokenTransferFromRequest;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.web3j.crypto.RawTransaction;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.IOException;
import java.math.BigInteger;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({OkHttpClient.class,Call.class,okhttp3.Response.class,okhttp3.Request.class})
public class HancockEthereumClientIntegrationTest {


//    @BeforeClass
//    public static void setUp() throws Exception{
//      HancockConfig mockConfig = mock(HancockConfig.class);
//      
//      mockedHancockEthereumClient = new HancockEthereumClient(mockConfig);
//      //MockWebServer server = new MockWebServer();
//  
//    }

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
        String data = "whatever";

        EthereumRawTransaction rawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value, data);

        assertTrue("RawTransaction is well constructed ", rawTransaction instanceof EthereumRawTransaction);
        assertTrue("RawTransaction web3 instance is well constructed ", rawTransaction.getWeb3Instance() instanceof RawTransaction);
        assertTrue("RawTransaction has nonce ", rawTransaction.getNonce() instanceof BigInteger);
        assertEquals(rawTransaction.getNonce(), nonce);
        assertTrue("RawTransaction has gasPrice ", rawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has gasLimit ", rawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has to ", rawTransaction.getTo() instanceof String);
        assertEquals(rawTransaction.getTo(), to);
        assertTrue("RawTransaction has value ", rawTransaction.getValue() instanceof BigInteger);
        assertEquals(rawTransaction.getValue(), value);
        assertTrue("RawTransaction has value ", rawTransaction.getData() instanceof String);
        assertEquals(rawTransaction.getData(), data);


        rawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value);

        assertTrue("RawTransaction has value ", rawTransaction.getValue() instanceof BigInteger);
        assertTrue("RawTransaction has value ", rawTransaction.getData() == "");
        assertEquals(rawTransaction.getNonce(), nonce);


        rawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, data);

        assertTrue("RawTransaction has value ", rawTransaction.getValue().equals(BigInteger.ZERO));
        assertTrue("RawTransaction has value ", rawTransaction.getData() instanceof String);
        assertEquals(rawTransaction.getNonce(), nonce);

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
        assertEquals(signedTransaction.substring(0, 6), "0xf860");

        System.out.println("Signed tx =>" + signedTransaction);

    }

    @Test public void testAdaptTransfer() throws Exception {

      BigInteger nonce = BigInteger.valueOf(1);
      BigInteger gasPrice = BigInteger.valueOf(111);
      BigInteger gasLimit = BigInteger.valueOf(222);
      BigInteger value = BigInteger.valueOf(333);
      String to = "0xmockAddress";
      String data = "0xwhatever";

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

      EthereumTransactionResponse responseModel= PowerMockito.mock(EthereumTransactionResponse.class);

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
//      EthereumTransactionResponse responseModel = gson.fromJson(responseBuilder.build().body().string(), EthereumTransactionResponse.class);
      PowerMockito.doReturn(responseModel).when(spy_var).checkStatus(any(okhttp3.Response.class), eq(EthereumTransactionResponse.class));
      PowerMockito.when(responseModel.getNonce()).thenReturn(nonce);
      PowerMockito.when(responseModel.getGasPrice()).thenReturn(gasPrice);
      PowerMockito.when(responseModel.getGas()).thenReturn(gasLimit);
      PowerMockito.when(responseModel.getTo()).thenReturn(to);
      PowerMockito.when(responseModel.getValue()).thenReturn(value);
      PowerMockito.when(responseModel.getData()).thenReturn(data);
      EthereumRawTransaction rawtx = spy_var.adaptTransfer(transferRequest);

      assertTrue("transaction adapted successfully", rawtx instanceof EthereumRawTransaction);

      System.out.println("rawtx =>" + rawtx);
      assertEquals(rawtx.getGasPrice(), gasPrice);
      assertEquals(rawtx.getTo(), to);
      assertEquals(rawtx.getValue(), value);
      assertEquals(rawtx.getNonce(), nonce);

  }

    @Test public void testTransfer() throws Exception {

         TransactionConfig txConfig = new TransactionConfig.Builder()
             .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
             .build();

         EthereumTransferRequest txRequest = new EthereumTransferRequest(
            "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
            "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d",
            new BigInteger("0260941720000000000").toString(),
            "test test"
         );
         
         Request.Builder requestBuilder = new Request.Builder();
         requestBuilder.get();
         requestBuilder.url("http://localhost");
         
         Response.Builder responseBuilder = new Response.Builder();
         responseBuilder.code(200);
         responseBuilder.protocol(Protocol.HTTP_1_1);
         responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"dat\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4A817C800\",\"gas\": \"0xc7c5\",\"to\": \"0xe3aee62f5bb4abab8b614fd80f1d92dbdbfd2f9a\",\"nonce\": \"0x3a\"}"));
         responseBuilder.request(requestBuilder.build());
         responseBuilder.message("Smart Contract - Success");
         
         BigInteger nonce = BigInteger.valueOf(1);
         BigInteger gasPrice = BigInteger.valueOf(111);
         BigInteger gasLimit = BigInteger.valueOf(222);
         BigInteger value = BigInteger.valueOf(333);
         String to = "0xmockAddress";
         String data = "0xwhatever";
         EthereumRawTransaction mockedEthereumRawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value);
         
         EthereumTransactionResponse responseTransfer= PowerMockito.mock(EthereumTransactionResponse.class);
         HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
         HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
         PowerMockito.doReturn(responseBuilder.build()).when(spy_var).makeCall(any(okhttp3.Request.class));
         PowerMockito.doReturn(responseTransfer).when(spy_var).checkStatus(any(okhttp3.Response.class), eq(EthereumTransactionResponse.class));
         
         PowerMockito.when(responseTransfer.getNonce()).thenReturn(nonce);
         PowerMockito.when(responseTransfer.getGasPrice()).thenReturn(gasPrice);
         PowerMockito.when(responseTransfer.getGas()).thenReturn(gasLimit);
         PowerMockito.when(responseTransfer.getTo()).thenReturn(to);
         PowerMockito.when(responseTransfer.getValue()).thenReturn(value);
         PowerMockito.when(responseTransfer.getData()).thenReturn(data);
         
         PowerMockito.whenNew(EthereumRawTransaction.class).withAnyArguments().thenReturn(mockedEthereumRawTransaction);
         
         PowerMockito.doReturn("mockSignedTransactionLocal").when(spy_var).sendSignedTransactionLocally(any(String.class), any(String.class));
         
         String rawtx = spy_var.transfer(txRequest, txConfig);

         assertTrue("transaction adapted successfully", rawtx instanceof String);
         assertEquals(rawtx, "mockSignedTransactionLocal");

         System.out.println("rawtx transaction =>" + rawtx);

     }

    @Test public void testTokenTransfer() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        EthereumTokenTransferRequest txRequest = new EthereumTokenTransferRequest(
                "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
                "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d",
                new BigInteger("0260941720000000000").toString(),
                "mockedAlias"
        );

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"dat\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4A817C800\",\"gas\": \"0xc7c5\",\"to\": \"0xe3aee62f5bb4abab8b614fd80f1d92dbdbfd2f9a\",\"nonce\": \"0x3a\"}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");

        BigInteger nonce = BigInteger.valueOf(1);
        BigInteger gasPrice = BigInteger.valueOf(111);
        BigInteger gasLimit = BigInteger.valueOf(222);
        BigInteger value = BigInteger.valueOf(333);
        String to = "0xmockAddress";
        String data = "0xwhatever";
        EthereumRawTransaction mockedEthereumRawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value);

        EthereumTransactionResponse responseTransfer= PowerMockito.mock(EthereumTransactionResponse.class);
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
        PowerMockito.doReturn(responseBuilder.build()).when(spy_var).makeCall(any(okhttp3.Request.class));
        PowerMockito.doReturn(responseTransfer).when(spy_var).checkStatus(any(okhttp3.Response.class), eq(EthereumTransactionResponse.class));

        PowerMockito.when(responseTransfer.getNonce()).thenReturn(nonce);
        PowerMockito.when(responseTransfer.getGasPrice()).thenReturn(gasPrice);
        PowerMockito.when(responseTransfer.getGas()).thenReturn(gasLimit);
        PowerMockito.when(responseTransfer.getTo()).thenReturn(to);
        PowerMockito.when(responseTransfer.getValue()).thenReturn(value);
        PowerMockito.when(responseTransfer.getData()).thenReturn(data);

        PowerMockito.whenNew(EthereumRawTransaction.class).withAnyArguments().thenReturn(mockedEthereumRawTransaction);

        PowerMockito.doReturn("mockSignedTransactionLocal").when(spy_var).sendSignedTransactionLocally(any(String.class), any(String.class));

        String rawtx = spy_var.tokenTransfer(txRequest, txConfig);

        assertTrue("transaction adapted successfully", rawtx instanceof String);
        assertEquals(rawtx, "mockSignedTransactionLocal");

        System.out.println("rawtx transaction =>" + rawtx);

    }

    @Test public void testTokenTransferFrom() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        EthereumTokenTransferFromRequest txRequest = new EthereumTokenTransferFromRequest(
                "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
                "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
                "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d",
                new BigInteger("0260941720000000000").toString(),
                "mockedAlias"
        );

        Request requestMock = new Request.Builder()
                .get()
                .url("http://localhost")
                .build();

        Response responseMock = new Response.Builder()
                .code(200)
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(MediaType.parse("application/json"), "{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"dat\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4A817C800\",\"gas\": \"0xc7c5\",\"to\": \"0xe3aee62f5bb4abab8b614fd80f1d92dbdbfd2f9a\",\"nonce\": \"0x3a\"}"))
                .request(requestMock)
                .message("Smart Contract - Success")
                .build();

        BigInteger nonce = BigInteger.valueOf(1);
        BigInteger gasPrice = BigInteger.valueOf(111);
        BigInteger gasLimit = BigInteger.valueOf(222);
        BigInteger value = BigInteger.valueOf(333);
        String to = "0xmockAddress";
        String data = "0xwhatever";
        EthereumRawTransaction mockedEthereumRawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value);

        EthereumTransactionResponse responseTransfer = PowerMockito.mock(EthereumTransactionResponse.class);
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spyHancockClient = PowerMockito.spy(auxHancockEthereumClient);

        PowerMockito
                .doReturn(responseMock)
                .when(spyHancockClient)
                .makeCall(any(okhttp3.Request.class));

        PowerMockito
                .doReturn(responseTransfer)
                .when(spyHancockClient)
                .checkStatus(any(okhttp3.Response.class), eq(EthereumTransactionResponse.class));

        PowerMockito.when(responseTransfer.getNonce()).thenReturn(nonce);
        PowerMockito.when(responseTransfer.getGasPrice()).thenReturn(gasPrice);
        PowerMockito.when(responseTransfer.getGas()).thenReturn(gasLimit);
        PowerMockito.when(responseTransfer.getTo()).thenReturn(to);
        PowerMockito.when(responseTransfer.getValue()).thenReturn(value);
        PowerMockito.when(responseTransfer.getData()).thenReturn(data);

        PowerMockito
                .whenNew(EthereumRawTransaction.class)
                .withAnyArguments()
                .thenReturn(mockedEthereumRawTransaction);

        PowerMockito
                .doReturn("mockSignedTransactionLocal")
                .when(spyHancockClient)
                .sendSignedTransactionLocally(any(String.class), any(String.class));

        String rawtx = spyHancockClient.tokenTransferFrom(txRequest, txConfig);

        assertTrue("transaction adapted successfully", rawtx instanceof String);
        assertEquals(rawtx, "mockSignedTransactionLocal");

        System.out.println("rawtx transaction =>" + rawtx);

    }

    @Test public void testTokenAllowance() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        EthereumTokenAllowanceRequest txRequest = new EthereumTokenAllowanceRequest(
                "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
                "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
                "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d",
                "mockedAlias"
        );

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"dat\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4A817C800\",\"gas\": \"0xc7c5\",\"to\": \"0xe3aee62f5bb4abab8b614fd80f1d92dbdbfd2f9a\",\"nonce\": \"0x3a\"}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");

        BigInteger nonce = BigInteger.valueOf(1);
        BigInteger gasPrice = BigInteger.valueOf(111);
        BigInteger gasLimit = BigInteger.valueOf(222);
        BigInteger value = BigInteger.valueOf(333);
        String to = "0xmockAddress";
        String data = "0xwhatever";
        EthereumRawTransaction mockedEthereumRawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value);

        EthereumTransactionResponse responseAllowance= PowerMockito.mock(EthereumTransactionResponse.class);
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
        PowerMockito.doReturn(responseBuilder.build()).when(spy_var).makeCall(any(okhttp3.Request.class));
        PowerMockito.doReturn(responseAllowance).when(spy_var).checkStatus(any(okhttp3.Response.class), eq(EthereumTransactionResponse.class));

        PowerMockito.when(responseAllowance.getNonce()).thenReturn(nonce);
        PowerMockito.when(responseAllowance.getGasPrice()).thenReturn(gasPrice);
        PowerMockito.when(responseAllowance.getGas()).thenReturn(gasLimit);
        PowerMockito.when(responseAllowance.getTo()).thenReturn(to);
        PowerMockito.when(responseAllowance.getValue()).thenReturn(value);
        PowerMockito.when(responseAllowance.getData()).thenReturn(data);

        PowerMockito.whenNew(EthereumRawTransaction.class).withAnyArguments().thenReturn(mockedEthereumRawTransaction);

        PowerMockito.doReturn("mockSignedTransactionLocal").when(spy_var).sendSignedTransactionLocally(any(String.class), any(String.class));

        String rawtx = spy_var.tokenAllowance(txRequest, txConfig);

        assertTrue("allowance adapted successfully", rawtx instanceof String);
        assertEquals(rawtx, "mockSignedTransactionLocal");

    }
    
    @Test public void testTokenApprove() throws Exception {

      TransactionConfig txConfig = new TransactionConfig.Builder()
              .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
              .build();

      EthereumTokenApproveRequest txRequest = new EthereumTokenApproveRequest(
              "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
              "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
              "10",
              "mockedAlias"
      );

      Request.Builder requestBuilder = new Request.Builder();
      requestBuilder.get();
      requestBuilder.url("http://localhost");

      Response.Builder responseBuilder = new Response.Builder();
      responseBuilder.code(200);
      responseBuilder.protocol(Protocol.HTTP_1_1);
      responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"dat\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4A817C800\",\"gas\": \"0xc7c5\",\"to\": \"0xe3aee62f5bb4abab8b614fd80f1d92dbdbfd2f9a\",\"nonce\": \"0x3a\"}"));
      responseBuilder.request(requestBuilder.build());
      responseBuilder.message("Smart Contract - Success");

      BigInteger nonce = BigInteger.valueOf(1);
      BigInteger gasPrice = BigInteger.valueOf(111);
      BigInteger gasLimit = BigInteger.valueOf(222);
      BigInteger value = BigInteger.valueOf(333);
      String to = "0xmockAddress";
      String data = "0xwhatever";
      EthereumRawTransaction mockedEthereumRawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value);

      EthereumTransactionResponse responseApprovee= PowerMockito.mock(EthereumTransactionResponse.class);
      HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
      HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
      PowerMockito.doReturn(responseBuilder.build()).when(spy_var).makeCall(any(okhttp3.Request.class));
      PowerMockito.doReturn(responseApprovee).when(spy_var).checkStatus(any(okhttp3.Response.class), eq(EthereumTransactionResponse.class));

      PowerMockito.when(responseApprovee.getNonce()).thenReturn(nonce);
      PowerMockito.when(responseApprovee.getGasPrice()).thenReturn(gasPrice);
      PowerMockito.when(responseApprovee.getGas()).thenReturn(gasLimit);
      PowerMockito.when(responseApprovee.getTo()).thenReturn(to);
      PowerMockito.when(responseApprovee.getValue()).thenReturn(value);
      PowerMockito.when(responseApprovee.getData()).thenReturn(data);

      PowerMockito.whenNew(EthereumRawTransaction.class).withAnyArguments().thenReturn(mockedEthereumRawTransaction);

      PowerMockito.doReturn("mockSignedTransactionLocal").when(spy_var).sendSignedTransactionLocally(any(String.class), any(String.class));

      String rawtx = spy_var.tokenApprove(txRequest, txConfig);

      assertTrue("approve adapted successfully", rawtx instanceof String);
      assertEquals(rawtx, "mockSignedTransactionLocal");

    }
    
    @Test public void testGetBalance() throws Exception {

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
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"balance\": \"10000\"}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");


        GetBalanceResponse responseModel= PowerMockito.mock(GetBalanceResponse.class);
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
        PowerMockito.doReturn(responseBuilder.build()).when(spy_var).makeCall(any(okhttp3.Request.class));
        PowerMockito.doReturn(responseModel).when(spy_var).checkStatus(any(okhttp3.Response.class), eq(GetBalanceResponse.class));
        PowerMockito.when(responseModel.getBalance()).thenReturn("0");


        BigInteger balance = spy_var.getBalance("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        assertTrue("transaction signed successfully", balance instanceof BigInteger);
        assertEquals(balance, BigInteger.valueOf(0));
        System.out.println("Balance =>" + balance.toString());

    }

    @Test public void testGetTokenBalance() throws Exception {

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
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"token\": \"10000\"\"balance\": \"10000\"}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");


        TokenBalanceResponse aux = PowerMockito.mock(TokenBalanceResponse.class);
        GetTokenBalanceResponse responseModel= PowerMockito.mock(GetTokenBalanceResponse.class);
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
        PowerMockito.doReturn(responseBuilder.build()).when(spy_var).makeCall(any(okhttp3.Request.class));
        PowerMockito.doReturn(responseModel).when(spy_var).checkStatus(any(okhttp3.Response.class), eq(GetTokenBalanceResponse.class));
        PowerMockito.when(responseModel.getTokenBalance()).thenReturn(aux);
        PowerMockito.when(aux.getBalance()).thenReturn(BigInteger.valueOf(0));


        TokenBalanceResponse balance = spy_var.getTokenBalance("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d", "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        assertTrue("transaction signed successfully", balance instanceof TokenBalanceResponse);
        assertEquals(balance.getBalance(), BigInteger.valueOf(0));
        System.out.println("Token Balance =>" + balance.getBalance().toString());

    }

    @Test public void testGetTokenMetadata() throws Exception {

        HancockConfig config = new HancockConfig.Builder()
                .withAdapter("http://localhost","", 3004)
                .build();

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"name\": \"mockedName\",\"symbol\": \"mockedSymbol\",\"decimals\": 10 ,\"totalSupply\": 10000}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");


        GetTokenMetadataResponseData aux = PowerMockito.mock(GetTokenMetadataResponseData.class);
        GetTokenMetadataResponse responseModel= PowerMockito.mock(GetTokenMetadataResponse.class);
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
        PowerMockito.doReturn(responseBuilder.build()).when(spy_var).makeCall(any(okhttp3.Request.class));
        PowerMockito.doReturn(responseModel).when(spy_var).checkStatus(any(okhttp3.Response.class), eq(GetTokenMetadataResponse.class));
        PowerMockito.when(responseModel.getTokenMetadata()).thenReturn(aux);
        PowerMockito.when(aux.getName()).thenReturn("mockedName");
        PowerMockito.when(aux.getSymbol()).thenReturn("mockedSymbol");
        PowerMockito.when(aux.getDecimals()).thenReturn(10);
        PowerMockito.when(aux.getTotalSupply()).thenReturn(10000);


        GetTokenMetadataResponseData metadata = spy_var.getTokenMetadata("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        assertTrue("metadata obtained successfully", metadata instanceof GetTokenMetadataResponseData);
        assertEquals(metadata.getName(), "mockedName");
        assertEquals(metadata.getSymbol(), "mockedSymbol");
        assertEquals(metadata.getDecimals(), Integer.valueOf(10));
        assertEquals(metadata.getTotalSupply(), Integer.valueOf(10000));


    }

    @Test (expected = IOException.class)
    public void testGetBalanceFail() throws Exception {

        HancockConfig config = new HancockConfig.Builder()
                .withAdapter("http://localhost","", 3004)
                .build();
        //HancockEthereumClient classUnderTest = new HancockEthereumClient(config);

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(400);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"balance\": \"10000\"}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Fail");


        GetBalanceResponse responseModel= PowerMockito.mock(GetBalanceResponse.class);
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
        PowerMockito.doReturn(responseBuilder.build()).when(spy_var).makeCall(any(okhttp3.Request.class));


        BigInteger balance = spy_var.getBalance("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

    }

    @Test public void testDecodeProtocol() throws Exception {

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

        System.out.println("Action Decode=>" + response.toString());

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

        System.out.println("Action Encode=>" + response.toString());

    }

    @Test public void testTokenRegister() throws Exception {

        HancockConfig config = new HancockConfig.Builder()
                .withAdapter("http://localhost","", 3004)
                .build();

        Request request = new Request.Builder()
                .get()
                .url("http://localhost")
                .build();

        Response response = new Response.Builder()
                .code(200)
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(MediaType.parse("application/json"), "{\"result\":{\"code\":200,\"description\":\"Token Register - Success\"}}"))
                .request(request)
                .message("Token Register - Success")
                .build();

        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spyHancockClient=PowerMockito.spy(auxHancockEthereumClient);

        PowerMockito.doReturn(response).when(spyHancockClient).makeCall(any(okhttp3.Request.class));

        HancockTokenRegisterResponse result = spyHancockClient.tokenRegister("mocked-alias", "0x1234");

        assertTrue("token registered successfully", result instanceof HancockTokenRegisterResponse);

    }


}