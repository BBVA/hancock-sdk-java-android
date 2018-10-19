package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.models.TransactionConfig;
import okhttp3.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;


@PowerMockIgnore({"javax.net.ssl.*"})
@RunWith(PowerMockRunner.class)
public class EthereumTransactionServiceIntegrationTest {


    public static EthereumTransaction txRequest;
    public static Response mockedResponse;
    public static EthereumTransactionService spyTransactionClient;
    public static EthereumTransactionResponse mockedTransactionResponse;

    @BeforeClass
    public static void setUp() throws Exception {

        txRequest = new EthereumTransaction(
                "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
                "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d",
                "1234",
                "12334142",
                "0x012345",
                "0260941720000000000",
                "31000"
        );

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"success\": \"true\"}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        mockedResponse = responseBuilder.build();

        mockedTransactionResponse = new EthereumTransactionResponse(true);

        HancockConfig auxConfig = new HancockConfig.Builder().build();

        spyTransactionClient = spy(new EthereumTransactionService(auxConfig));

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any(), any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse);

    }

    @PrepareForTest({ Common.class})
    @Test public void testSendWithSendRawTx() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .build();

        EthereumTransactionResponse rawtx = spyTransactionClient.send(txRequest, txConfig);

        verify(spyTransactionClient).sendRawTransaction(eq(txRequest));
        assertTrue("transaction adapted successfully", rawtx instanceof EthereumTransactionResponse);
        assertEquals(rawtx.getSuccess(), mockedTransactionResponse.getSuccess());

    }

    @PrepareForTest({ Common.class})
    @Test public void testSendWithSendSigned() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        EthereumTransactionResponse rawtx = spyTransactionClient.send(txRequest, txConfig);

        verify(spyTransactionClient).signTransaction(any(EthereumRawTransaction.class), eq("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8"));
        verify(spyTransactionClient).sendSignedTransaction(any(String.class), eq(txConfig));
        assertTrue("transaction adapted successfully", rawtx instanceof EthereumTransactionResponse);
        assertEquals(rawtx.getSuccess(), mockedTransactionResponse.getSuccess());

    }

    @PrepareForTest({ Common.class})
    @Test public void testSendWithSendToSign() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withProvider("mockProvider")
                .build();

        EthereumTransactionResponse rawtx = spyTransactionClient.send(txRequest, txConfig);

        verify(spyTransactionClient).sendToSignProvider(eq(txRequest), eq(txConfig));
        assertTrue("transaction adapted successfully", rawtx instanceof EthereumTransactionResponse);
        assertEquals(rawtx.getSuccess(), mockedTransactionResponse.getSuccess());

    }

}
