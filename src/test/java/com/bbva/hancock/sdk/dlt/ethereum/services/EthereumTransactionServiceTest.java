package com.bbva.hancock.sdk.dlt.ethereum.services;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumWallet;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.models.TransactionConfig;

import org.junit.BeforeClass;
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

//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.stubbing.Answer;

@PowerMockIgnore("javax.net.ssl.*")
//@RunWith(MockitoJUnitRunner.class)
@RunWith(PowerMockRunner.class)
public class EthereumTransactionServiceTest {

    public static HancockConfig mockedConfig;
    public static EthereumWallet mockedWallet;
    public static EthereumTransaction mockedEthereumRawTransaction;
    public static EthereumTransferRequest mockedEthereumTransferRequest;
    public static EthereumTransactionService mockedHancockEthereumClient;
    public static Common commonAux;
    public static EthereumTransactionResponse mockedEthereumTransactionResponse;
    public static Web3j mockedWeb3;

    @BeforeClass
    public static void setUp() throws Exception{
      
        mockedConfig = new HancockConfig.Builder()
                .withEnv("custom")
                .withNode("http://mock.node.com", 9999)
                .withAdapter("http://mock.adapter.com", "/base", 9999)
                .withWallet("http://mock.wallet.com", "/base", 9999)
                .build();
        mockedHancockEthereumClient = new EthereumTransactionService(mockedConfig);
        mockedWallet = new EthereumWallet("0xmockAddress","mockPrivateKey","mockPublicKey");
        mockedEthereumTransactionResponse = new EthereumTransactionResponse(true);

        BigInteger nonce = BigInteger.valueOf(1);
        BigInteger gasPrice = BigInteger.valueOf(111);
        BigInteger gasLimit = BigInteger.valueOf(222);
        BigInteger value = BigInteger.valueOf(333);
        String from = mockedWallet.getAddress();
        String to = mockedWallet.getAddress();
        String data = "0xwhatever";

        mockedEthereumTransferRequest = new EthereumTransferRequest(from, to, value.toString(), data);
        mockedEthereumRawTransaction = new EthereumTransaction(from, to, value.toString(), nonce.toString(), gasLimit.toString(), gasPrice.toString());
        commonAux = new Common();

    }

    @PrepareForTest({Request.class, Web3jFactory.class,RawTransaction.class})
    @Test public void testSendSignedTransactionLocally() throws Exception {

        mockedWeb3 = mock(Web3j.class);
        
        PowerMockito.mockStatic(Web3jFactory.class);
        when(Web3jFactory.build(any(HttpService.class))).thenReturn(mockedWeb3);       
        
        EthSendTransaction mockTransaction = new EthSendTransaction();
        mockTransaction.setResult("MockResult");
        
        @SuppressWarnings("rawtypes")
        Future mockFuture = mock(Future.class);
        
        @SuppressWarnings("rawtypes")
        Request mockRequest = mock(Request.class);
        
        PowerMockito.doReturn(mockTransaction)
        .when(mockFuture)
        .get();
        
        PowerMockito.doReturn(mockFuture)
        .when(mockRequest)
        .sendAsync();
        
        PowerMockito.doReturn(mockRequest)
        .when(mockedWeb3)
        .ethSendRawTransaction(any(String.class));
        
        EthereumTransactionResponse signedTransaction = mockedHancockEthereumClient.sendSignedTransactionLocally("mockedsignedTransaction");
        assertTrue("message signed successfully", signedTransaction instanceof EthereumTransactionResponse);
        assertEquals(signedTransaction.getSuccess(),  true);

    }
    
    @PrepareForTest({Common.class, Credentials.class, TransactionEncoder.class,RawTransaction.class})
    @Test public void testSignTransaction() throws Exception {

        PowerMockito.mockStatic(Credentials.class);
        //when(Credentials.create("mockPrivateKey")).thenReturn(mock(Credentials.class));
        when(Credentials.class, "create", "mockPrivateKey").thenReturn(mock(Credentials.class));

        PowerMockito.mockStatic(RawTransaction.class);
        EthereumRawTransaction mockEthereumRawTransaction = mock(EthereumRawTransaction.class);
        when(mockEthereumRawTransaction.getWeb3Instance()).thenReturn(mock(RawTransaction.class));

        byte[] aux = "mockedsignedTransaction".getBytes();

        PowerMockito.mockStatic(TransactionEncoder.class);
        when(TransactionEncoder.signMessage(any(RawTransaction.class), any(Credentials.class))).thenReturn(aux);

        String signedTransaction = mockedHancockEthereumClient.signTransaction(mockEthereumRawTransaction,"mockPrivateKey");
        assertTrue("transaction signed successfully", signedTransaction instanceof String);
        assertEquals(signedTransaction,  Numeric.toHexString(aux));

    }

    @PrepareForTest({Common.class})
    @Test public void testSendSignedTransaction() throws Exception {

        okhttp3.Request requestMock= mock(okhttp3.Request.class);
        okhttp3.Response responseMock= mock(okhttp3.Response.class);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(okhttp3.RequestBody.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumTransactionResponse.class))
                .thenReturn(mockedEthereumTransactionResponse);

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        EthereumTransactionService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        EthereumTransactionResponse mockResult = spy_var.sendSignedTransaction("mockSignedTransaction", txConfig);

        assertEquals(mockResult, mockedEthereumTransactionResponse);
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);

    }

    @PrepareForTest({Common.class})
    @Test public void testSendToSignProvider() throws Exception {

        okhttp3.Request requestMock= mock(okhttp3.Request.class);
        okhttp3.Response responseMock= mock(okhttp3.Response.class);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(okhttp3.RequestBody.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumTransactionResponse.class))
                .thenReturn(mockedEthereumTransactionResponse);

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withProvider("mockProvider")
                .build();

        EthereumTransactionService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        EthereumTransactionResponse mockResult = spy_var.sendToSignProvider(mockedEthereumRawTransaction, txConfig);

        assertEquals(mockResult, mockedEthereumTransactionResponse);
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);

    }

    @PrepareForTest({Common.class})
    @Test public void testSendRawTransaction() throws Exception {

        okhttp3.Request requestMock= mock(okhttp3.Request.class);
        okhttp3.Response responseMock= mock(okhttp3.Response.class);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(okhttp3.RequestBody.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumTransactionResponse.class))
                .thenReturn(mockedEthereumTransactionResponse);

        EthereumTransactionService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        EthereumTransactionResponse mockResult = spy_var.sendRawTransaction(mockedEthereumRawTransaction);

        assertEquals(mockResult, mockedEthereumTransactionResponse);
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);

    }



    @PrepareForTest({Common.class})
    @Test public void testSendWithoutTxConfig() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .build();

        EthereumTransactionService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        PowerMockito.doReturn(mockedEthereumTransactionResponse)
                .when(spy_var)
                .sendRawTransaction(any(EthereumTransaction.class));

        EthereumTransactionResponse mockResult = spy_var.send(mockedEthereumRawTransaction, txConfig);

        assertEquals(mockResult, mockedEthereumTransactionResponse);
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);

    }

}
