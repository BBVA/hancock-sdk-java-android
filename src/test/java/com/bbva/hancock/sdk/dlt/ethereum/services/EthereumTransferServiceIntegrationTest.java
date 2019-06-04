package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.models.TransactionConfig;
import okhttp3.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@PrepareForTest({Common.class})
@RunWith(PowerMockRunner.class)
public class EthereumTransferServiceIntegrationTest {

    public static String nonce;
    public static String gasPrice;
    public static String gasLimit;
    public static String value;
    public static String to;
    public static String from;
    public static String data;
    public static String requestContent;
    public static Response mockedResponse;
    public static Request.Builder requestBuilder;
    public static HancockConfig mockedConfig;

    @Before
    public void setUp() {

        nonce = "0x1";
        gasPrice = "0x4";
        gasLimit = "0x3";
        value = "0x2";
        to = "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d";
        from = "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e";
        data = "0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001";
        requestContent = "{\"data\":{\"from\": \"" + from + "\",\"data\": \"" + data + "\",\"gasPrice\": \"" + gasPrice + "\",\"gas\": \"" + gasLimit + "\",\"value\": \"" + value + "\",\"to\": \"" + to + "\",\"nonce\": \"" + nonce + "\"}}";

        requestBuilder = new Request.Builder();

        requestBuilder.get();
        requestBuilder.url("http://localhost");

        final Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), requestContent));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        mockedResponse = responseBuilder.build();

        mockedConfig = new HancockConfig.Builder().build();
    }


    @Test
    public void testAdaptTransfer() throws Exception {

        final EthereumTransferRequest transferRequest = new EthereumTransferRequest(from, to, value);

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any(), any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse);

        final EthereumTransactionService transactionClient = new EthereumTransactionService(mockedConfig);
        final EthereumTransferService auxHancockEthereumTokenClient = new EthereumTransferService(mockedConfig, transactionClient);
        final EthereumTransferService spy_var = spy(auxHancockEthereumTokenClient);

        final EthereumTransaction rawtx = spy_var.adaptTransfer(transferRequest);

        assertTrue("transaction adapted successfully", rawtx instanceof EthereumTransaction);
        assertEquals(rawtx.getGasPrice(), gasPrice);
        assertEquals(rawtx.getTo(), to);
        assertEquals(rawtx.getValue(), value);
        assertEquals(rawtx.getNonce(), nonce);

    }

    @Test
    public void testSend() throws Exception {

        final TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        final EthereumTransferRequest transferRequest = new EthereumTransferRequest(from, to, value);

        final Response.Builder responseBuilder2 = new Response.Builder();
        responseBuilder2.code(200);
        responseBuilder2.protocol(Protocol.HTTP_1_1);
        responseBuilder2.body(ResponseBody.create(MediaType.parse("application/json"), "{\"success\": \"true\"}"));
        responseBuilder2.request(requestBuilder.build());
        responseBuilder2.message("Smart Contract - Success");
        final Response mockedResponse2 = responseBuilder2.build();

        final EthereumTransactionResponse mockedTransactionResponse = new EthereumTransactionResponse(true);

        final EthereumTransactionService transactionClient = new EthereumTransactionService(mockedConfig);
        final EthereumTransactionService spyTransactionClient = spy(transactionClient);
        final EthereumTransferService auxEthereumTransferClient = new EthereumTransferService(mockedConfig, spyTransactionClient);

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

        final EthereumTransactionResponse rawtx = auxEthereumTransferClient.send(transferRequest, txConfig);

        verify(spyTransactionClient).send(any(EthereumTransaction.class), eq(txConfig));
        assertTrue("transaction adapted successfully", rawtx instanceof EthereumTransactionResponse);
        assertEquals(rawtx.getSuccess(), mockedTransactionResponse.getSuccess());

    }

}
