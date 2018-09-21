package com.bbva.hancock.sdk.dlt.ethereum.clients;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumWallet;
import com.bbva.hancock.sdk.dlt.ethereum.models.util.ValidateParameters;
import com.bbva.hancock.sdk.dlt.ethereum.models.wallet.GetBalanceResponse;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3jFactory;

import java.math.BigInteger;

import okhttp3.Call;
import okhttp3.OkHttpClient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.stubbing.Answer;

@PowerMockIgnore("javax.net.ssl.*")
//@RunWith(MockitoJUnitRunner.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest({TransactionEncoder.class,Keys.class,Credentials.class,EthereumRawTransaction.class,RawTransaction.class,Web3jFactory.class,
        OkHttpClient.class,Call.class,okhttp3.Response.class,okhttp3.Request.class,GetBalanceResponse.class,HancockConfig.class, ValidateParameters.class, Common.class})
public class HancockEthereumWalletClientTest {

    public static HancockConfig mockedConfig;
    public static EthereumWallet mockedWallet;
    public static ECKeyPair mockedEcKeyPair;
    public static Credentials mockedCredentials;
    public static HancockEthereumWalletClient mockedHancockEthereumClient;

    @BeforeClass
    public static void setUp() throws Exception{

        mockedConfig = new HancockConfig.Builder()
                .withEnv("custom")
                .withNode("http://mock.node.com", 9999)
                .withAdapter("http://mock.adapter.com", "/base", 9999)
                .build();

        mockedEcKeyPair = new ECKeyPair(new BigInteger("0"), new BigInteger("1"));
        mockedCredentials = Credentials.create(mockedEcKeyPair);
        mockedHancockEthereumClient = new HancockEthereumWalletClient(mockedConfig);

    }

    @Test public void testGenerateWallet() throws Exception {

        HancockEthereumWalletClient spy_wallet_var = PowerMockito.spy(mockedHancockEthereumClient);

        mockStatic(Keys.class);
        PowerMockito.when(Keys.class, "createEcKeyPair").thenReturn(mockedEcKeyPair);

        mockStatic(Credentials.class);
        PowerMockito.when(Credentials.class, "create", any(ECKeyPair.class)).thenReturn(mockedCredentials);

        mockedWallet = spy_wallet_var.generateWallet();

        assertTrue("Wallet should have an address", mockedWallet.getAddress() instanceof String);
        assertTrue("Wallet should have a publicKey", mockedWallet.getPublicKey() instanceof String);
        assertTrue("Wallet should have a privateKey", mockedWallet.getPrivateKey() instanceof String);

        assertEquals(mockedWallet.getAddress(), "0x7f7915573c7e97b47efa546f5f6e3230263bcb49");
        assertEquals(mockedWallet.getPrivateKey(), "0x0");
        assertEquals(mockedWallet.getPublicKey(), "0x1");

    }

    @Test public void testGetBalance() throws Exception {

        mockStatic(ValidateParameters.class);
        PowerMockito.doNothing().when(ValidateParameters.class, "checkAddress", any(String.class));
        PowerMockito.doNothing().when(ValidateParameters.class, "checkForContent", any(String.class) , any(String.class));

        GetBalanceResponse responseModel = mock(GetBalanceResponse.class);
        when(responseModel.getBalance()).thenReturn("0");
        okhttp3.Response responseMock = mock(okhttp3.Response.class);
        okhttp3.Request requestMock = mock(okhttp3.Request.class);

        HancockEthereumWalletClient spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class)).thenReturn(requestMock);
        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class)).thenReturn(responseMock);
        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(GetBalanceResponse.class)).thenReturn(responseModel);

        BigInteger balance = spy_var.getBalance("mockAddress");

        assertTrue("Wallet should have a Balance", balance instanceof BigInteger);
        assertEquals(balance, BigInteger.valueOf(0));

    }

}
