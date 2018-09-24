package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumWallet;
import com.bbva.hancock.sdk.dlt.ethereum.models.wallet.GetBalanceResponse;
import com.bbva.hancock.sdk.exception.HancockException;
import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


@PowerMockIgnore({"javax.net.ssl.*"})
@RunWith(PowerMockRunner.class)
public class HancockEthereumWalletClientIntegrationTest {

    @PrepareForTest({Common.class})
    @Test public void testCreateRawTransaction() throws Exception {

        HancockEthereumWalletClient classUnderTest = new HancockEthereumWalletClient();
        EthereumWallet wallet = classUnderTest.generateWallet();

        BigInteger nonce = BigInteger.valueOf(1);
        BigInteger gasPrice = BigInteger.valueOf(111);
        BigInteger gasLimit = BigInteger.valueOf(222);
        BigInteger value = BigInteger.valueOf(333);
        String to = wallet.getAddress();
        String data = "whatever";

        EthereumRawTransaction rawTransaction = new EthereumRawTransaction(to, nonce, value, data, gasPrice, gasLimit);

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


        rawTransaction = new EthereumRawTransaction(to, nonce, value, gasPrice, gasLimit);

        assertTrue("RawTransaction has value ", rawTransaction.getValue() instanceof BigInteger);
        assertEquals(rawTransaction.getNonce(), nonce);


        rawTransaction = new EthereumRawTransaction(to, nonce, new BigInteger("0"), data, gasPrice, gasLimit);

        assertTrue("RawTransaction has value ", rawTransaction.getValue().equals(BigInteger.ZERO));
        assertTrue("RawTransaction has value ", rawTransaction.getData() instanceof String);
        assertEquals(rawTransaction.getNonce(), nonce);

    }

    @Test public void testGenerateWallet() throws Exception {

        HancockEthereumWalletClient classUnderTest = new HancockEthereumWalletClient();
        EthereumWallet wallet = classUnderTest.generateWallet();
        assertNotNull("Wallet should have an address", wallet.getAddress());
        assertNotNull("Wallet should have an address", wallet.getPublicKey());
        assertNotNull("Wallet should have an address", wallet.getPrivateKey());

    }

    @PrepareForTest({Keys.class})
    @Test (expected = HancockException.class)
    public void testGenerateWalletFail() throws Exception {

        PowerMockito.mockStatic(Keys.class);
        PowerMockito.when(Keys.createEcKeyPair()).thenThrow(new InvalidAlgorithmParameterException());

        HancockEthereumWalletClient classUnderTest = new HancockEthereumWalletClient();
        EthereumWallet wallet = classUnderTest.generateWallet();

    }

    @PrepareForTest({Common.class})
    @Test public void testGetBalance() throws Exception {

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"data\":{\"balance\": \"10000\"}}"));
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


        GetBalanceResponse responseModel= PowerMockito.mock(GetBalanceResponse.class);
        HancockEthereumWalletClient auxHancockEthereumWalletClient = new HancockEthereumWalletClient();
        HancockEthereumWalletClient spy_var=PowerMockito.spy(auxHancockEthereumWalletClient);

        PowerMockito.when(responseModel.getBalance()).thenReturn("10000");


        BigInteger balance = spy_var.getBalance("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        assertTrue("transaction signed successfully", balance instanceof BigInteger);
        assertEquals(balance, BigInteger.valueOf(10000));

    }

    @PrepareForTest({Common.class})
    @Test (expected = HancockException.class)
    public void testGetBalanceFail() throws Exception {

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(500);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"error\": \"500\",\"internalError\": \"HKWH50002\",\"message\": \"Can not fetch SignProvider\",\"extendedMessage\": \"MongoError: there are no users authenticated\"}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Fail");
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

        HancockEthereumWalletClient auxHancockEthereumWalletClient = new HancockEthereumWalletClient();
        HancockEthereumWalletClient spy_var=PowerMockito.spy(auxHancockEthereumWalletClient);


        BigInteger balance = spy_var.getBalance("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

    }

    @Test (expected = HancockException.class)
    public void testGetBalanceParameterFail() throws Exception {

        HancockEthereumWalletClient auxHancockEthereumWalletClient = new HancockEthereumWalletClient();
        HancockEthereumWalletClient spy_var=PowerMockito.spy(auxHancockEthereumWalletClient);

        BigInteger balance = spy_var.getBalance("");

    }

}
