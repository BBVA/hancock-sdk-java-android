package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumWallet;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.models.TransactionConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.powermock.api.mockito.PowerMockito.*;


@PrepareForTest({Common.class, Request.class, Web3jFactory.class, RawTransaction.class, Credentials.class, TransactionEncoder.class})
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.net.ssl.*"})
public class EthereumTransactionServiceTest {

    public static HancockConfig mockedConfig;
    public static EthereumWallet mockedWallet;
    public static EthereumTransaction mockedEthereumRawTransaction;
    public static EthereumTransferRequest mockedEthereumTransferRequest;
    public static EthereumTransactionService mockedHancockEthereumClient;
    public static Common commonAux;
    public static EthereumTransactionResponse mockedEthereumTransactionResponse;
    public static Web3j mockedWeb3;

    @Before
    public void setUp() throws Exception {

        mockedConfig = new HancockConfig.Builder()
                .withEnv("custom")
                .withNode("http://mock.node.com", 9999)
                .withAdapter("http://mock.adapter.com", "/base", 9999)
                .withWallet("http://mock.wallet.com", "/base", 9999)
                .build();
        mockedHancockEthereumClient = new EthereumTransactionService(mockedConfig);
        mockedWallet = new EthereumWallet("0xmockAddress", "mockPrivateKey", "mockPublicKey");
        mockedEthereumTransactionResponse = new EthereumTransactionResponse(true);

        final BigInteger nonce = BigInteger.valueOf(1);
        final BigInteger gasPrice = BigInteger.valueOf(111);
        final BigInteger gasLimit = BigInteger.valueOf(222);
        final BigInteger value = BigInteger.valueOf(333);
        final String from = mockedWallet.getAddress();
        final String to = mockedWallet.getAddress();
        final String data = "0xwhatever";

        mockedEthereumTransferRequest = new EthereumTransferRequest(from, to, value.toString(), data);
        mockedEthereumRawTransaction = new EthereumTransaction(from, to, value.toString(), nonce.toString(), gasLimit.toString(), gasPrice.toString());
        commonAux = new Common();

    }


    @Test
    public void testSendSignedTransactionLocally() throws Exception {

        mockedWeb3 = mock(Web3j.class);

        PowerMockito.mockStatic(Web3jFactory.class);
        when(Web3jFactory.build(any(HttpService.class))).thenReturn(mockedWeb3);

        final EthSendTransaction mockTransaction = new EthSendTransaction();
        mockTransaction.setResult("MockResult");

        final Future mockFuture = mock(Future.class);

        final Request mockRequest = mock(Request.class);

        PowerMockito.doReturn(mockTransaction)
                .when(mockFuture)
                .get();

        PowerMockito.doReturn(mockFuture)
                .when(mockRequest)
                .sendAsync();

        PowerMockito.doReturn(mockRequest)
                .when(mockedWeb3)
                .ethSendRawTransaction(any(String.class));

        final EthereumTransactionResponse signedTransaction = mockedHancockEthereumClient.sendSignedTransactionLocally("mockedsignedTransaction");
        assertTrue("message signed successfully", signedTransaction instanceof EthereumTransactionResponse);
        assertTrue(signedTransaction.getSuccess());

    }

    @Test
    public void testSignTransaction() throws Exception {

        PowerMockito.mockStatic(Credentials.class);
        when(Credentials.class, "create", "mockPrivateKey").thenReturn(mock(Credentials.class));

        PowerMockito.mockStatic(RawTransaction.class);
        final EthereumRawTransaction mockEthereumRawTransaction = mock(EthereumRawTransaction.class);
        when(mockEthereumRawTransaction.getWeb3Instance()).thenReturn(mock(RawTransaction.class));

        final byte[] aux = "mockedsignedTransaction".getBytes();

        PowerMockito.mockStatic(TransactionEncoder.class);
        when(TransactionEncoder.signMessage(any(RawTransaction.class), any(Credentials.class))).thenReturn(aux);

        final String signedTransaction = mockedHancockEthereumClient.signTransaction(mockEthereumRawTransaction, "mockPrivateKey");
        assertTrue("transaction signed successfully", signedTransaction instanceof String);
        assertEquals(signedTransaction, Numeric.toHexString(aux));

    }

    @Test
    public void testSendSignedTransaction() throws Exception {

        final okhttp3.Request requestMock = mock(okhttp3.Request.class);
        final okhttp3.Response responseMock = mock(okhttp3.Response.class);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(okhttp3.RequestBody.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumTransactionResponse.class))
                .thenReturn(mockedEthereumTransactionResponse);

        final TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        final EthereumTransactionService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        final EthereumTransactionResponse mockResult = spy_var.sendSignedTransaction("mockSignedTransaction", txConfig);

        assertEquals(mockResult, mockedEthereumTransactionResponse);
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);

    }

    @Test
    public void testSendToSignProvider() throws Exception {

        final okhttp3.Request requestMock = mock(okhttp3.Request.class);
        final okhttp3.Response responseMock = mock(okhttp3.Response.class);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(okhttp3.RequestBody.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumTransactionResponse.class))
                .thenReturn(mockedEthereumTransactionResponse);

        final TransactionConfig txConfig = new TransactionConfig.Builder()
                .withProvider("mockProvider")
                .build();

        final EthereumTransactionService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        final EthereumTransactionResponse mockResult = spy_var.sendToSignProvider(mockedEthereumRawTransaction, txConfig);

        assertEquals(mockResult, mockedEthereumTransactionResponse);
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);

    }

    @Test
    public void testSendRawTransaction() throws Exception {

        final okhttp3.Request requestMock = mock(okhttp3.Request.class);
        final okhttp3.Response responseMock = mock(okhttp3.Response.class);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(okhttp3.RequestBody.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumTransactionResponse.class))
                .thenReturn(mockedEthereumTransactionResponse);

        final EthereumTransactionService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        final EthereumTransactionResponse mockResult = spy_var.sendRawTransaction(mockedEthereumRawTransaction);

        assertEquals(mockResult, mockedEthereumTransactionResponse);
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);

    }

    @Test
    public void testSendWithoutTxConfig() throws Exception {

        final TransactionConfig txConfig = new TransactionConfig.Builder()
                .build();

        final EthereumTransactionService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        PowerMockito.doReturn(mockedEthereumTransactionResponse)
                .when(spy_var)
                .sendRawTransaction(any(EthereumTransaction.class));

        final EthereumTransactionResponse mockResult = spy_var.send(mockedEthereumRawTransaction, txConfig);

        assertEquals(mockResult, mockedEthereumTransactionResponse);
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);

    }

}
