package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.TransactionConfig;
import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.*;

@PowerMockIgnore({"javax.net.ssl.*"})
@RunWith(PowerMockRunner.class)
public class EthereumTransferClientIntegrationTest {

    @PrepareForTest({Common.class})
    @Test public void testAdaptTransfer() throws Exception {

        String nonce = "0x1";
        String gasPrice = "0x4";
        String gasLimit = "0x3";
        String value = "0x2";
        String to = "0xmockAddress";
        String data = "0xwhatever";

        EthereumTransferRequest transferRequest = new EthereumTransferRequest(
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
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"data\":{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"data\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4\",\"gas\": \"0x3\",\"value\": \"0x2\",\"to\": \"0xmockAddress\",\"nonce\": \"0x1\"}}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        Response mockedResponse = responseBuilder.build();

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any(), any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse);

        EthereumTransactionClient transactionClient = new EthereumTransactionClient();
        EthereumTransferClient auxHancockEthereumTokenClient = new EthereumTransferClient(transactionClient);
        EthereumTransferClient spy_var = spy(auxHancockEthereumTokenClient);

        EthereumTransaction rawtx = spy_var.adaptTransfer(transferRequest);

        assertTrue("transaction adapted successfully", rawtx instanceof EthereumTransaction);
        assertEquals(rawtx.getGasPrice(), gasPrice);
        assertEquals(rawtx.getTo(), to);
        assertEquals(rawtx.getValue(), value);
        assertEquals(rawtx.getNonce(), nonce);

    }

    @PrepareForTest({Common.class})
    @Test public void testSend() throws Exception {

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
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"data\":{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"data\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4\",\"gas\": \"0x3\",\"value\": \"0x2\",\"to\": \"0xmockAddress\",\"nonce\": \"0x1\"}}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        Response mockedResponse = responseBuilder.build();

        Response.Builder responseBuilder2 = new Response.Builder();
        responseBuilder2.code(200);
        responseBuilder2.protocol(Protocol.HTTP_1_1);
        responseBuilder2.body(ResponseBody.create(MediaType.parse("application/json"), "{\"success\": \"true\"}"));
        responseBuilder2.request(requestBuilder.build());
        responseBuilder2.message("Smart Contract - Success");
        Response mockedResponse2 = responseBuilder2.build();

        EthereumTransactionResponse mockedTransactionResponse = new EthereumTransactionResponse(true);

        EthereumTransactionClient transactionClient = new EthereumTransactionClient();
        EthereumTransferClient auxEthereumTransferClient = new EthereumTransferClient(transactionClient);

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

        EthereumTransactionResponse rawtx = auxEthereumTransferClient.send(txRequest, txConfig);

        assertTrue("transaction adapted successfully", rawtx instanceof EthereumTransactionResponse);
        assertEquals(rawtx.getSuccess(), mockedTransactionResponse.getSuccess());

    }

}
