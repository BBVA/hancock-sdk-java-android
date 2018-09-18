package com.bbva.hancock.sdk.dlt.ethereum.clients;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolAction;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolDecodeResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolDlt;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolEncodeRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolEncodeResponse;
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
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3jFactory;

import java.math.BigInteger;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@PowerMockIgnore("javax.net.ssl.*")
//@RunWith(MockitoJUnitRunner.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest({TransactionEncoder.class,Credentials.class,EthereumRawTransaction.class,RawTransaction.class,Web3jFactory.class,
        OkHttpClient.class,Call.class,okhttp3.Response.class,okhttp3.Request.class,GetBalanceResponse.class,HancockConfig.class, ValidateParameters.class, Common.class})
public class HancockEthereumProtocolClientTest {

    public static HancockConfig mockedConfig;
    public static HancockEthereumProtocolClient mockedHancockEthereumClient;

    @BeforeClass
    public static void setUp() throws Exception{

        mockedConfig = new HancockConfig.Builder()
                .withEnv("custom")
                .withNode("http://mock.node.com", 9999)
                .withAdapter("http://mock.adapter.com", "/base", 9999)
                .build();
        mockedHancockEthereumClient = new HancockEthereumProtocolClient();

    }
  
    @Test public void testDecodeProtocol() throws Exception {

        okhttp3.Request requestMock = mock(okhttp3.Request.class);
        okhttp3.Response responseMock = mock(okhttp3.Response.class);
        HancockProtocolDecodeResponse responseModel = mock(HancockProtocolDecodeResponse.class);
        HancockEthereumProtocolClient auxHancockEthereumClient = new HancockEthereumProtocolClient();
        HancockEthereumProtocolClient spy_var = PowerMockito.spy(auxHancockEthereumClient);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(okhttp3.RequestBody.class)).thenReturn(requestMock);
        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class)).thenReturn(responseMock);
        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(HancockProtocolDecodeResponse.class)).thenReturn(responseModel);
        PowerMockito.when(Common.class, "getResourceUrl", any(HancockConfig.class), any(String.class)).thenReturn("mockUrl");

        HancockProtocolDecodeResponse mockResult = spy_var.decodeProtocol("mockedCode");
        System.out.println("transaction decode: ");
        assertTrue("transaction decode successfully", mockResult instanceof HancockProtocolDecodeResponse);


    }

    @Test public void testEncodeProtocol() throws Exception {

        okhttp3.Request requestMock = mock(okhttp3.Request.class);
        okhttp3.Response responseMock = mock(okhttp3.Response.class);
        HancockProtocolEncodeResponse responseModel = mock(HancockProtocolEncodeResponse.class);
        HancockEthereumProtocolClient auxHancockEthereumClient = new HancockEthereumProtocolClient();
        PowerMockito.whenNew(HancockProtocolEncodeRequest.class).withAnyArguments().thenReturn(mock(HancockProtocolEncodeRequest.class));

        HancockEthereumProtocolClient spy_var = PowerMockito.spy(auxHancockEthereumClient);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(okhttp3.RequestBody.class)).thenReturn(requestMock);
        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class)).thenReturn(responseMock);
        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(HancockProtocolEncodeResponse.class)).thenReturn(responseModel);
        PowerMockito.when(Common.class, "getResourceUrl", any(HancockConfig.class), any(String.class)).thenReturn("mockUrl");

        HancockProtocolEncodeResponse mockResult = spy_var.encodeProtocol(HancockProtocolAction.transfer, new BigInteger("10"), "0x1234", "dafsda", HancockProtocolDlt.ethereum);

        assertTrue("transaction encode successfully", mockResult instanceof HancockProtocolEncodeResponse);
        System.out.println("transaction encode: ");

    }

}
