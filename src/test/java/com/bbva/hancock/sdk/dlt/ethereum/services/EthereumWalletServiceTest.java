package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumWallet;
import com.bbva.hancock.sdk.dlt.ethereum.models.wallet.GetBalanceResponse;
import com.bbva.hancock.sdk.util.ValidateParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.powermock.api.mockito.PowerMockito.*;

@PrepareForTest({Credentials.class, org.web3j.crypto.Keys.class, ValidateParameters.class, Common.class})
@RunWith(PowerMockRunner.class)
public class EthereumWalletServiceTest {

    public static HancockConfig mockedConfig;
    public static EthereumWallet mockedWallet;
    public static EthereumWalletService mockedHancockEthereumClient;

    @Before
    public void setUp() {

        mockedConfig = new HancockConfig.Builder()
                .withEnv("custom")
                .withNode("http://mock.node.com", 9999)
                .withAdapter("http://mock.adapter.com", "/base", 9999)
                .build();

        final EthereumWalletService hancockEthereumClient = new EthereumWalletService(mockedConfig);

        mockedHancockEthereumClient = PowerMockito.spy(hancockEthereumClient);
    }

    @Test
    public void testGenerateWallet() throws Exception {

        final ECKeyPair mockECKeyPair = mock(ECKeyPair.class);
        PowerMockito.when(mockECKeyPair.getPrivateKey()).thenReturn(BigInteger.ZERO);
        PowerMockito.when(mockECKeyPair.getPublicKey()).thenReturn(BigInteger.ONE);

        mockStatic(Keys.class);
        PowerMockito.when(Keys.class, "createEcKeyPair").thenReturn(mockECKeyPair);

        final Credentials mockCredentials = mock(Credentials.class);
        mockStatic(Credentials.class);
        PowerMockito.when(Credentials.class, "create", mockECKeyPair).thenReturn(mockCredentials);

        PowerMockito.when(mockCredentials.getAddress()).thenReturn("mockedAddress");

        PowerMockito.when(mockCredentials.getEcKeyPair()).thenReturn(mockECKeyPair);

        mockedWallet = mockedHancockEthereumClient.generateWallet();

        assertNotNull("Wallet should have an address", mockedWallet.getAddress());
        assertNotNull("Wallet should have a publicKey", mockedWallet.getPublicKey());
        assertNotNull("Wallet should have a privateKey", mockedWallet.getPrivateKey());

        assertEquals(mockedWallet.getAddress(), "mockedAddress");
        assertEquals(mockedWallet.getPrivateKey(), "0x0");
        assertEquals(mockedWallet.getPublicKey(), "0x1");

    }

    @Test
    public void testGetBalance() throws Exception {

        mockStatic(ValidateParameters.class);
        PowerMockito.doNothing().when(ValidateParameters.class, "checkAddress", any(String.class));
        PowerMockito.doNothing().when(ValidateParameters.class, "checkForContent", any(String.class), any(String.class));

        final GetBalanceResponse responseModel = mock(GetBalanceResponse.class);
        when(responseModel.getBalance()).thenReturn("0");
        final okhttp3.Response responseMock = mock(okhttp3.Response.class);
        final okhttp3.Request requestMock = mock(okhttp3.Request.class);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class)).thenReturn(requestMock);
        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class)).thenReturn(responseMock);
        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(GetBalanceResponse.class)).thenReturn(responseModel);

        final BigInteger balance = mockedHancockEthereumClient.getBalance("mockAddress");

        assertNotNull("Wallet should have a Balance", balance);
        assertEquals(balance, BigInteger.valueOf(0));

    }

}
