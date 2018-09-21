package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumWallet;
import com.bbva.hancock.sdk.dlt.ethereum.models.wallet.GetBalanceResponse;
import com.bbva.hancock.sdk.exception.HancockException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.web3j.crypto.Keys;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


@PowerMockIgnore({"javax.net.ssl.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({OkHttpClient.class,Call.class,Response.class,Request.class,Keys.class, Common.class})
public class HancockEthereumWalletClientIntegrationTest {

    @Test public void testGenerateWallet() throws Exception {

        HancockEthereumWalletClient classUnderTest = new HancockEthereumWalletClient();
        EthereumWallet wallet = classUnderTest.generateWallet();
        assertTrue("Wallet should have an address", wallet.getAddress() instanceof String);
        assertTrue("Wallet should have an address", wallet.getPublicKey() instanceof String);
        assertTrue("Wallet should have an address", wallet.getPrivateKey() instanceof String);

    }

    @Test (expected = HancockException.class)
    public void testGenerateWalletFail() throws Exception {

        PowerMockito.mockStatic(Keys.class);
        PowerMockito.when(Keys.createEcKeyPair()).thenThrow(new InvalidAlgorithmParameterException());

        HancockEthereumWalletClient classUnderTest = new HancockEthereumWalletClient();
        EthereumWallet wallet = classUnderTest.generateWallet();

    }

    @Test public void testGetBalance() throws Exception {

        HancockConfig config = new HancockConfig.Builder()
                .withAdapter("http://localhost","", 3004)
                .build();

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

    @Test (expected = HancockException.class)
    public void testGetBalanceFail() throws Exception {

        HancockConfig config = new HancockConfig.Builder()
                .withAdapter("http://localhost","", 3004)
                .build();

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
