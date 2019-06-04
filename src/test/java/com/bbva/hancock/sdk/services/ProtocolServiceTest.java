package com.bbva.hancock.sdk.services;

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.models.protocol.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Common.class})
public class ProtocolServiceTest {

    public static HancockConfig mockedConfig;
    public static ProtocolService mockedHancockEthereumClient;

    @Before
    public void setUp() {

        mockedConfig = new HancockConfig.Builder()
                .withEnv("custom")
                .withNode("http://mock.node.com", 9999)
                .withAdapter("http://mock.adapter.com", "/base", 9999)
                .build();
        mockedHancockEthereumClient = new ProtocolService(mockedConfig);

    }

    @Test
    public void testDecodeProtocol() throws Exception {

        final okhttp3.Request requestMock = mock(okhttp3.Request.class);
        final okhttp3.Response responseMock = mock(okhttp3.Response.class);

        final HancockProtocolDecodeResponse responseModel = new HancockProtocolDecodeResponse();
        final ProtocolService auxHancockEthereumClient = new ProtocolService(mockedConfig);
        final ProtocolService spy_var = PowerMockito.spy(auxHancockEthereumClient);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(okhttp3.RequestBody.class)).thenReturn(requestMock);
        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class)).thenReturn(responseMock);
        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(HancockProtocolDecodeResponse.class)).thenReturn(responseModel);
        PowerMockito.when(Common.class, "getResourceUrl", any(HancockConfig.class), any(String.class)).thenReturn("mockUrl");

        final HancockProtocolDecodeResponse mockResult = spy_var.decodeProtocol("mockedCode");

        assertTrue("transaction decode successfully", mockResult instanceof HancockProtocolDecodeResponse);

    }

    @Test
    public void testEncodeProtocol() throws Exception {

        final okhttp3.Request requestMock = mock(okhttp3.Request.class);
        final okhttp3.Response responseMock = mock(okhttp3.Response.class);
        final HancockProtocolEncodeResponse responseModel = mock(HancockProtocolEncodeResponse.class);
        final ProtocolService auxHancockEthereumClient = new ProtocolService(mockedConfig);
        PowerMockito.whenNew(HancockProtocolEncodeRequest.class).withAnyArguments().thenReturn(mock(HancockProtocolEncodeRequest.class));

        final ProtocolService spy_var = PowerMockito.spy(auxHancockEthereumClient);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(okhttp3.RequestBody.class)).thenReturn(requestMock);
        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class)).thenReturn(responseMock);
        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(HancockProtocolEncodeResponse.class)).thenReturn(responseModel);
        PowerMockito.when(Common.class, "getResourceUrl", any(HancockConfig.class), any(String.class)).thenReturn("mockUrl");

        final HancockProtocolEncodeResponse mockResult = spy_var.encodeProtocol(HancockProtocolAction.transfer, BigInteger.TEN, "0x1234", "dafsda", HancockProtocolDlt.ethereum);

        assertTrue("transaction encode successfully", mockResult instanceof HancockProtocolEncodeResponse);

    }

}
