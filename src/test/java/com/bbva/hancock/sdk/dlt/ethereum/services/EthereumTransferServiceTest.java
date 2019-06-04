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
import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;


@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({Common.class})
public class EthereumTransferServiceTest {

    public static HancockConfig mockedConfig;
    public static EthereumWallet mockedWallet;
    public static EthereumRawTransaction mockedEthereumRawTransaction;
    public static EthereumTransaction mockedEthereumTransaction;
    public static EthereumTransferRequest mockedEthereumTransferRequest;
    public static EthereumTransferService mockedHancockEthereumClient;
    public static EthereumTransactionService mockedHancockEthereumClientTransaction;
    public static EthereumTransactionService spy_transaction_var;

    @Before
    public void setUp() throws Exception {

        mockedConfig = new HancockConfig.Builder()
                .withEnv("custom")
                .withNode("http://mock.node.com", 9999)
                .withAdapter("http://mock.adapter.com", "/base", 9999)
                .build();

        mockedHancockEthereumClientTransaction = new EthereumTransactionService(mockedConfig);
        spy_transaction_var = PowerMockito.spy(mockedHancockEthereumClientTransaction);
        mockedHancockEthereumClient = new EthereumTransferService(mockedConfig, spy_transaction_var);
        mockedWallet = new EthereumWallet("0xmockAddress", "mockPrivateKey", "mockPublicKey");

        final BigInteger nonce = BigInteger.valueOf(1);
        final BigInteger gasPrice = BigInteger.valueOf(111);
        final BigInteger gasLimit = BigInteger.valueOf(222);
        final BigInteger value = BigInteger.valueOf(333);
        final String from = mockedWallet.getAddress();
        final String to = mockedWallet.getAddress();
        final String data = "0xwhatever";

        mockedEthereumTransferRequest = new EthereumTransferRequest(from, to, value.toString(), data);
        mockedEthereumRawTransaction = new EthereumRawTransaction(to, nonce, value, gasPrice, gasLimit);
        mockedEthereumTransaction = new EthereumTransaction(from, to, value.toString(), nonce.toString(), gasLimit.toString(), gasPrice.toString());

    }

    @Test
    public void testCreateRawTransaction() {

        assertTrue("RawTransaction is well constructed ", mockedEthereumRawTransaction instanceof EthereumRawTransaction);
        assertTrue("RawTransaction web3 instance is well constructed ", mockedEthereumRawTransaction.getWeb3Instance() instanceof RawTransaction);
        assertTrue("RawTransaction has nonce ", mockedEthereumRawTransaction.getNonce() instanceof BigInteger);
        assertTrue("RawTransaction has gasPrice ", mockedEthereumRawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has gasLimit ", mockedEthereumRawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has to ", mockedEthereumRawTransaction.getTo() instanceof String);
        assertTrue("RawTransaction has value ", mockedEthereumRawTransaction.getValue() instanceof BigInteger);

    }

    @Test
    public void testSend() throws Exception {
        final TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();


        final EthereumTransferService spy_transfer_var = PowerMockito.spy(mockedHancockEthereumClient);

        PowerMockito.doReturn(mock(EthereumTransaction.class))
                .when(spy_transfer_var)
                .adaptTransfer(any(EthereumTransferRequest.class));

        PowerMockito.doReturn(mock(EthereumTransactionResponse.class))
                .when(spy_transaction_var)
                .send(any(EthereumTransaction.class), any(TransactionConfig.class));

        final EthereumTransactionResponse mockResult = spy_transfer_var.send(mockedEthereumTransferRequest, txConfig);

        verify(spy_transfer_var).adaptTransfer(eq(mockedEthereumTransferRequest));
        verify(spy_transaction_var).send(any(EthereumTransaction.class), eq(txConfig));
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);
    }

}
