package com.bbva.hancock.sdk.dlt.ethereum.clients;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumWallet;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.TransactionConfig;
import okhttp3.RequestBody;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)
public class EthereumTransferClientTest {

    public static HancockConfig mockedConfig;
    public static EthereumWallet mockedWallet;
    public static EthereumRawTransaction mockedEthereumRawTransaction;
    public static EthereumTransaction mockedEthereumTransaction;
    public static EthereumTransferRequest mockedEthereumTransferRequest;
    public static EthereumTransferClient mockedHancockEthereumClient;
    public static EthereumTransactionClient mockedHancockEthereumClientTransaction;
    public static EthereumTransactionClient spy_transaction_var;

    @BeforeClass
    public static void setUp() throws Exception{

        mockedConfig = new HancockConfig.Builder()
                .withEnv("custom")
                .withNode("http://mock.node.com", 9999)
                .withAdapter("http://mock.adapter.com", "/base", 9999)
                .build();

        mockedHancockEthereumClientTransaction = new EthereumTransactionClient(mockedConfig);
        spy_transaction_var = PowerMockito.spy(mockedHancockEthereumClientTransaction);
        mockedHancockEthereumClient = new EthereumTransferClient(mockedConfig, spy_transaction_var);
        mockedWallet = new EthereumWallet("0xmockAddress","mockPrivateKey","mockPublicKey");

        BigInteger nonce = BigInteger.valueOf(1);
        BigInteger gasPrice = BigInteger.valueOf(111);
        BigInteger gasLimit = BigInteger.valueOf(222);
        BigInteger value = BigInteger.valueOf(333);
        String from = mockedWallet.getAddress();
        String to = mockedWallet.getAddress();
        String data = "0xwhatever";

        mockedEthereumTransferRequest = new EthereumTransferRequest(from, to, value.toString(), data);
        mockedEthereumRawTransaction = new EthereumRawTransaction(to, nonce, value, gasPrice, gasLimit);
        mockedEthereumTransaction= new EthereumTransaction(from, to, value.toString(), nonce.toString(), gasLimit.toString(), gasPrice.toString());

    }

    @PrepareForTest({ Common.class})
    @Test public void testCreateRawTransaction() throws Exception {

        assertTrue("RawTransaction is well constructed ", mockedEthereumRawTransaction instanceof EthereumRawTransaction);
        assertTrue("RawTransaction web3 instance is well constructed ", mockedEthereumRawTransaction.getWeb3Instance() instanceof RawTransaction);
        assertTrue("RawTransaction has nonce ", mockedEthereumRawTransaction.getNonce() instanceof BigInteger);
        assertTrue("RawTransaction has gasPrice ", mockedEthereumRawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has gasLimit ", mockedEthereumRawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has to ", mockedEthereumRawTransaction.getTo() instanceof String);
        assertTrue("RawTransaction has value ", mockedEthereumRawTransaction.getValue() instanceof BigInteger);

    }

    @PrepareForTest({ Common.class})
    @Test public void testSend() throws Exception {
        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();


        EthereumTransferClient spy_transfer_var = PowerMockito.spy(mockedHancockEthereumClient);

        PowerMockito.doReturn(mock(EthereumTransaction.class))
                .when(spy_transfer_var)
                .adaptTransfer(any(EthereumTransferRequest.class));

        PowerMockito.doReturn(mock(EthereumTransactionResponse.class))
                .when(spy_transaction_var)
                .send(any(EthereumTransaction.class), any(TransactionConfig.class));

        EthereumTransactionResponse mockResult = spy_transfer_var.send(mockedEthereumTransferRequest, txConfig);

        verify(spy_transfer_var).adaptTransfer(eq(mockedEthereumTransferRequest));
        verify(spy_transaction_var).send(any(EthereumTransaction.class), eq(txConfig));
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);
    }

//    @PrepareForTest({ Common.class})
//    @Test public void testAdaptTransfer() throws Exception {
//
//        String nonce = "1";
//        String gasPrice = "111";
//        String gasLimit = "222";
//        String value = "333";
//        String to = mockedWallet.getAddress();
//        String data = "0xwhatever";
//
//        EthereumTransaction responseModel = mock(EthereumTransaction.class);
//        okhttp3.Request requestMock= mock(okhttp3.Request.class);
//        okhttp3.Response responseMock= mock(okhttp3.Response.class);
//
//        EthereumTransactionClient spy_transaction_var = PowerMockito.spy(mockedHancockEthereumClientTransaction);
//        EthereumTransferClient spy_transfer_var = PowerMockito.spy(mockedHancockEthereumClient);
//
//        mockStatic(Common.class);
//        when(Common.class, "getRequest", any(String.class), any(RequestBody.class)).thenReturn(requestMock);
//        when(Common.class, "makeCall", any(okhttp3.Request.class)).thenReturn(responseMock);
//        when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumTransaction.class)).thenReturn(responseModel);
//        when(Common.class, "getResourceUrl", any(HancockConfig.class), any(String.class)).thenReturn("mockUrl");
//
//        when(responseModel.getNonce()).thenReturn(nonce);
//        when(responseModel.getGasPrice()).thenReturn(gasPrice);
//        when(responseModel.getGas()).thenReturn(gasLimit);
//        when(responseModel.getTo()).thenReturn(to);
//        when(responseModel.getValue()).thenReturn(value);
//        when(responseModel.getData()).thenReturn(data);
//        PowerMockito.whenNew(EthereumTransaction.class).withAnyArguments().thenReturn(mockedEthereumTransaction);
//
//        EthereumTransaction transfer = spy_transfer_var.adaptTransfer(mockedEthereumTransferRequest);
//
//        assertTrue("Wallet should have a Balance", transfer instanceof EthereumTransaction);
//        assertEquals(transfer.getGasPrice(), mockedEthereumTransaction.getGasPrice());
//        assertEquals(transfer.getTo(), mockedEthereumTransaction.getTo());
//        assertEquals(transfer.getValue(), mockedEthereumTransaction.getValue());
//        assertEquals(transfer.getNonce(), mockedEthereumTransaction.getNonce());
//
//    }

}
