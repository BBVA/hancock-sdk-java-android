package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransactionAdaptResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumWallet;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumCallResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumRegisterResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.models.HancockGenericResponse;
import com.bbva.hancock.sdk.models.TransactionConfig;
import okhttp3.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.web3j.protocol.core.methods.response.AbiDefinition;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Common.class})
public class EthereumSmartContractServiceIntegrationTest {

    public static HancockConfig mockedConfig;
    public static TransactionConfig mockedTransactionConfig;
    public static EthereumWallet mockedWallet;
    public static EthereumSmartContractService mockedHancockEthereumClient;
    public static EthereumTransactionService spyTransactionService;
    public static EthereumSmartContractService spy_var;
    public static EthereumTransactionAdaptResponse mockedEthereumAdaptInvoke;
    public static EthereumTransaction mockedEthereumTransaction;
    public static EthereumTransactionResponse mockedEthereumTransactionResponse;

    public static String from;
    public static String to;
    public static String method;
    public static String data;
    public static String addressOrAlias;
    public static ArrayList<AbiDefinition> abi;
    public static ArrayList<String> params;

    @Before
    public void setUp() {

        mockedConfig = new HancockConfig.Builder()
                .withEnv("custom")
                .withNode("http://mock.node.com", 9999)
                .withAdapter("http://mock.adapter.com", "/base", 9999)
                .build();

        mockedTransactionConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        final EthereumTransactionService transactionClient = new EthereumTransactionService(mockedConfig);
        spyTransactionService = PowerMockito.spy(transactionClient);
        mockedHancockEthereumClient = new EthereumSmartContractService(mockedConfig, spyTransactionService);
        spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        mockedWallet = new EthereumWallet("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d", "mockPrivateKey", "mockPublicKey");

        final String nonce = String.valueOf(1);
        final String gasPrice = String.valueOf(111);
        final String gasLimit = String.valueOf(222);
        final String value = String.valueOf(333);
        from = mockedWallet.getAddress();
        to = mockedWallet.getAddress();
        abi = new ArrayList<>();
        method = "mockedMethod";
        addressOrAlias = "mockedAlias";
        params = new ArrayList<>();
        params.add("mockedFirtsParam");
        data = "mockedData";

        mockedEthereumTransaction = new EthereumTransaction(from, to, value, data, nonce, gasLimit, gasPrice);
        mockedEthereumAdaptInvoke = new EthereumTransactionAdaptResponse(mockedEthereumTransaction, new HancockGenericResponse(1, "mockedOK"));
        mockedEthereumTransactionResponse = new EthereumTransactionResponse(true);

    }

    @Test
    public void testInvoke() throws Exception {

        final Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        final Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"data\":{\"from\": \"0x6c0a14f7561898b9ddc0c57652a53b2c6665443e\",\"data\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4\",\"gas\": \"0x3\",\"value\": \"0x2\",\"to\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"nonce\": \"0x1\"}}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        final Response mockedResponse = responseBuilder.build();

        final Response.Builder responseBuilder2 = new Response.Builder();
        responseBuilder2.code(200);
        responseBuilder2.protocol(Protocol.HTTP_1_1);
        responseBuilder2.body(ResponseBody.create(MediaType.parse("application/json"), "{\"success\": \"true\"}"));
        responseBuilder2.request(requestBuilder.build());
        responseBuilder2.message("Smart Contract - Success");
        final Response mockedResponse2 = responseBuilder2.build();

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any(), any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse)
                .thenReturn(mockedResponse2);

        final EthereumTransactionResponse response = spy_var.invoke(addressOrAlias, method, params, from, mockedTransactionConfig);
        assertTrue("Response is of type TransactionResponse", response instanceof EthereumTransactionResponse);
        assertTrue(response.getSuccess());

    }

    @Test
    public void testInvokeAbi() throws Exception {

        final Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        final Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"data\":{\"from\": \"0x6c0a14f7561898b9ddc0c57652a53b2c6665443e\",\"data\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4\",\"gas\": \"0x3\",\"value\": \"0x2\",\"to\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"nonce\": \"0x1\"}}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        final Response mockedResponse = responseBuilder.build();

        final Response.Builder responseBuilder2 = new Response.Builder();
        responseBuilder2.code(200);
        responseBuilder2.protocol(Protocol.HTTP_1_1);
        responseBuilder2.body(ResponseBody.create(MediaType.parse("application/json"), "{\"success\": \"true\"}"));
        responseBuilder2.request(requestBuilder.build());
        responseBuilder2.message("Smart Contract - Success");
        final Response mockedResponse2 = responseBuilder2.build();

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any(), any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse)
                .thenReturn(mockedResponse2);

        final EthereumTransactionResponse response = spy_var.invokeAbi(addressOrAlias, method, params, from, mockedTransactionConfig, abi);
        assertTrue("Response is of type TransactionResponse", response instanceof EthereumTransactionResponse);
        assertTrue(response.getSuccess());

    }

    @Test
    public void testCallAbi() throws Exception {

        final Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        final Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"data\": \"mockedData\" ,\"result\":{\"code\": 1, \"description\": \"mockedDescription\"}}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        final Response mockedResponse = responseBuilder.build();

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any(), any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse);

        final EthereumCallResponse response = spy_var.callAbi(addressOrAlias, method, params, from, abi);
        assertTrue("Response is of type CallResponse", response instanceof EthereumCallResponse);
        assertEquals(response.getResult().getDescription(), "mockedDescription");
        assertEquals(response.getResult().getCode(), new Integer(1));

    }

    @Test
    public void testCall() throws Exception {

        final Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        final Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"data\": \"mockedData\" ,\"result\":{\"code\": 1, \"description\": \"mockedDescription\"}}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        final Response mockedResponse = responseBuilder.build();

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any(), any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse);

        final EthereumCallResponse response = spy_var.call(addressOrAlias, method, params, from);
        assertTrue("Response is of type CallResponse", response instanceof EthereumCallResponse);
        assertEquals(response.getResult().getDescription(), "mockedDescription");
        assertEquals(response.getResult().getCode(), new Integer(1));
    }

    @Test
    public void testRegister() throws Exception {

        final Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        final Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{ \"result\": {\"code\":1 , \"description\": \"mockedDescription\"}}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        final Response mockedResponse = responseBuilder.build();

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any(), any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse);

        final EthereumRegisterResponse response = spy_var.register(addressOrAlias, to, new ArrayList<>());
        assertTrue("Response is of type HancockGenericResponse", response instanceof EthereumRegisterResponse);
        assertEquals(response.getResult().getDescription(), "mockedDescription");
        assertEquals(response.getResult().getCode(), new Integer(1));

    }

    @Test
    public void testAdaptInvoke() throws Exception {

        final Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        final Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"data\": {\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"data\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"4\",\"gas\": \"3\",\"value\": \"2\",\"to\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"nonce\": \"1\"} ,\"result\":{\"code\": 1, \"description\": \"mockedDescription\"}}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        final Response mockedResponse = responseBuilder.build();

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any(), any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse);

        final EthereumTransactionAdaptResponse response = spy_var.adaptInvoke(addressOrAlias, method, params, from);
        assertTrue("Response is of type EthereumAdaptInvokeResponse", response instanceof EthereumTransactionAdaptResponse);
        assertEquals(response.getData().getFrom(), from);
        assertEquals(response.getData().getTo(), to);
        assertEquals(response.getData().getData(), "0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001");

    }

    @Test
    public void testAdaptInvokeAbi() throws Exception {

        final Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        final Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"data\": {\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"data\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"4\",\"gas\": \"3\",\"value\": \"2\",\"to\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"nonce\": \"1\"} ,\"result\":{\"code\": 1, \"description\": \"mockedDescription\"}}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        final Response mockedResponse = responseBuilder.build();

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any(), any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse);

        final Response response = spy_var.adaptInvokeAbi(addressOrAlias, method, params, from, "send", abi);
        assertTrue("Response is of type Response", response instanceof Response);
        assertEquals(response.code(), 200);
    }

}
