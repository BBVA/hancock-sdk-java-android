package com.bbva.hancock.sdk.dlt.ethereum.clients;//package com.bbva.hancock.sdk.dlt.ethereum.clients.integration;

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.allowance.EthereumTokenAllowanceRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.approve.EthereumTokenApproveRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.balance.EthereumTokenBalanceResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata.GetEthereumTokenMetadataResponseData;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.register.EthereumTokenRegisterResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.transfer.EthereumTokenTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.transferFrom.EthereumTokenTransferFromRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.TransactionConfig;
import okhttp3.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@PowerMockIgnore({"javax.net.ssl.*"})
@RunWith(PowerMockRunner.class)
public class EthereumTokenClientIntegrationTest {


    public static String requestContent;
    public static String nonce;
    public static String gasPrice;
    public static String gasLimit;
    public static String value;
    public static String to;
    public static String data;
    public static Response mockedResponse;
    public static Response mockedResponse2;
    public static EthereumTransactionClient spy_transaction_var;
    public static EthereumTokenClient spy_var;

    @BeforeClass
    public static void setUp(){

        nonce = "0x1";
        gasPrice = "0x4";
        gasLimit = "0x3";
        value = "0x2";
        to = "0xmockAddress";
        data = "0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001";

        requestContent = "{\"data\":{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"data\": \"" + data + "\",\"gasPrice\": \"" + gasPrice + "\",\"gas\": \"" + gasLimit + "\",\"value\": \"" + value + "\",\"to\": \"" + to + "\",\"nonce\": \"" + nonce + "\"}}";

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), requestContent));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        mockedResponse = responseBuilder.build();

        Response.Builder responseBuilder2 = new Response.Builder();
        responseBuilder2.code(200);
        responseBuilder2.protocol(Protocol.HTTP_1_1);
        responseBuilder2.body(ResponseBody.create(MediaType.parse("application/json"), "{\"success\": \"true\"}"));
        responseBuilder2.request(requestBuilder.build());
        responseBuilder2.message("Smart Contract - Success");
        mockedResponse2 = responseBuilder2.build();

        EthereumTransactionClient transactionClient = new EthereumTransactionClient();
        spy_transaction_var = spy(transactionClient);
        EthereumTokenClient auxEthereumTokenClient = new EthereumTokenClient(spy_transaction_var);
        spy_var = spy(auxEthereumTokenClient);
    }

    @PrepareForTest({ Common.class})
    @Test public void testAdaptTransfer() throws Exception {

        EthereumTokenRequest transferRequest = new EthereumTokenRequest(
                "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
                "tokenTransfer"
        );

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), requestContent));
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
        EthereumTokenClient auxEthereumTokenClient = new EthereumTokenClient(transactionClient);
        EthereumTokenClient spy_var = spy(auxEthereumTokenClient);

        EthereumTransaction rawtx = spy_var.adaptTransfer(transferRequest);

        assertTrue("transaction adapted successfully", rawtx instanceof EthereumTransaction);

        assertEquals(rawtx.getGasPrice(), gasPrice);
        assertEquals(rawtx.getTo(), to);
        assertEquals(rawtx.getValue(), value);
        assertEquals(rawtx.getNonce(), nonce);
        assertEquals(rawtx.getGas(), gasLimit);
        assertEquals(rawtx.getData(), data);

    }

    @PrepareForTest({ Common.class})
    @Test public void testTransfer() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .build();

        EthereumTokenTransferRequest txRequest = new EthereumTokenTransferRequest(
                "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
                "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d",
                new BigInteger("0260941720000000000").toString(),
                "test test"
        );

        EthereumTransactionResponse mockedTransactionResponse = new EthereumTransactionResponse(true);

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
        EthereumTransactionResponse rawtx = spy_var.transfer(txRequest, txConfig);

        assertTrue("transaction adapted successfully", rawtx instanceof EthereumTransactionResponse);
        verify(spy_transaction_var).send(any(EthereumTransaction.class), eq(txConfig));
        assertEquals(rawtx.getSuccess(), mockedTransactionResponse.getSuccess());

    }

    @PrepareForTest({ Common.class})
    @Test public void testTransferFrom() throws Exception {

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

        EthereumTransactionResponse mockedTransactionResponse = new EthereumTransactionResponse(true);

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

        EthereumTransactionResponse rawtx = spy_var.transferFrom(txRequest, txConfig);


        verify(spy_transaction_var).send(any(EthereumTransaction.class), eq(txConfig));
        assertTrue("transaction adapted successfully", rawtx instanceof EthereumTransactionResponse);
        assertEquals(rawtx.getSuccess(), mockedTransactionResponse.getSuccess());

    }

    @PrepareForTest({ Common.class})
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

        EthereumTransactionResponse mockedTransactionResponse = new EthereumTransactionResponse(true);
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

        EthereumTransactionResponse rawtx = spy_var.allowance(txRequest, txConfig);

        verify(spy_transaction_var).send(any(EthereumTransaction.class), eq(txConfig));
        assertTrue("transaction adapted successfully", rawtx instanceof EthereumTransactionResponse);
        assertEquals(rawtx.getSuccess(), mockedTransactionResponse.getSuccess());

    }

    @PrepareForTest({ Common.class})
    @Test public void testTokenApprove() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withProvider("mockedProvider")
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

        EthereumTransactionResponse mockedTransactionResponse = new EthereumTransactionResponse(true);

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

        EthereumTransactionResponse rawtx = spy_var.approve(txRequest, txConfig);

        assertTrue("transaction adapted successfully", rawtx instanceof EthereumTransactionResponse);
        verify(spy_transaction_var).send(any(EthereumTransaction.class), eq(txConfig));
        assertEquals(rawtx.getSuccess(), mockedTransactionResponse.getSuccess());

    }

    @PrepareForTest({ Common.class})
    @Test public void testGetTokenBalance() throws Exception {

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"data\":{\"tokenbalance\":{\"balance\":0 , \"decimals\":2}}}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        Response mockedResponse = responseBuilder.build();

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse);

        EthereumTransactionClient transactionClient = new EthereumTransactionClient();
        EthereumTokenClient auxEthereumTokenClient = new EthereumTokenClient(transactionClient);
        EthereumTokenClient spy_var = spy(auxEthereumTokenClient);


        EthereumTokenBalanceResponse balance = spy_var.getBalance("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d", "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        assertTrue("transaction signed successfully", balance instanceof EthereumTokenBalanceResponse);
        assertEquals(balance.getBalance(), BigInteger.valueOf(0));

    }

    @PrepareForTest({ Common.class})
    @Test public void testGetTokenMetadata() throws Exception {

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"data\":{ \"name\": \"mockedName\",\"symbol\": \"mockedSymbol\",\"decimals\": 10 ,\"totalSupply\": 10000}}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");

        Response mockedResponse = responseBuilder.build();

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse);

        EthereumTransactionClient transactionClient = new EthereumTransactionClient();
        EthereumTokenClient auxEthereumTokenClient = new EthereumTokenClient(transactionClient);
        EthereumTokenClient spy_var = spy(auxEthereumTokenClient);

        GetEthereumTokenMetadataResponseData metadata = spy_var.getMetadata("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        assertTrue("metadata obtained successfully", metadata instanceof GetEthereumTokenMetadataResponseData);
        assertEquals(metadata.getName(), "mockedName");
        assertEquals(metadata.getSymbol(), "mockedSymbol");
        assertEquals(metadata.getDecimals(), Integer.valueOf(10));
        assertEquals(metadata.getTotalSupply(), Integer.valueOf(10000));


    }

    @PrepareForTest({ Common.class})
    @Test public void testTokenRegister() throws Exception {

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

        EthereumTransactionClient transactionClient = new EthereumTransactionClient();
        EthereumTokenClient auxEthereumTokenClient = new EthereumTokenClient(transactionClient);
        EthereumTokenClient spy_var = spy(auxEthereumTokenClient);

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any(), any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(response);

        EthereumTokenRegisterResponse result = spy_var.register("mocked-alias", "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        assertTrue("token registered successfully", result instanceof EthereumTokenRegisterResponse);

    }


}
