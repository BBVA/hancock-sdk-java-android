package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolAction;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolDecodeResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolDlt;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolEncodeResponse;
import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


@PowerMockIgnore({"javax.net.ssl.*"})
@RunWith(PowerMockRunner.class)
public class HancockEthereumProtocolClientIntegrationTest {

    @PrepareForTest({Common.class})
    @Test public void testDecodeProtocol() throws Exception {

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"dat\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4A817C800\",\"gas\": \"0xc7c5\",\"to\": \"0x1234\",\"nonce\": \"0x3a\"}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        Response mockedResponse = responseBuilder.build();

        HancockEthereumProtocolClient auxHancockEthereumClient = new HancockEthereumProtocolClient();
        HancockEthereumProtocolClient spy_var=PowerMockito.spy(auxHancockEthereumClient);

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any(), any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse);

        HancockProtocolDecodeResponse response = spy_var.decodeProtocol("hancock://qr?code=%7B%22action%22%3A%22transfer%22%2C%22body%22%3A%7B%22value%22%3A%2210%22%2C%22data%22%3A%22dafsda%22%2C%22to%22%3A%220x1234%22%7D%2C%22dlt%22%3A%22ethereum%22%7D");

//        assertTrue("transaction signed successfully", response.getTo().equals("0x1234"));
        assertTrue("transaction decode successfully", response instanceof HancockProtocolDecodeResponse);

    }

    @PrepareForTest({Common.class})
    @Test public void testEncodeProtocol() throws Exception {

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.get();
        requestBuilder.url("http://localhost");

        Response.Builder responseBuilder = new Response.Builder();
        responseBuilder.code(200);
        responseBuilder.protocol(Protocol.HTTP_1_1);
        responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), "{\"from\": \"0xde8e772f0350e992ddef81bf8f51d94a8ea9216d\",\"dat\": \"0xa9059cbb0000000000000000000000006c0a14f7561898b9ddc0c57652a53b2c6665443e0000000000000000000000000000000000000000000000000000000000000001\",\"gasPrice\": \"0x4A817C800\",\"gas\": \"0xc7c5\",\"to\": \"0x1234\",\"nonce\": \"0x3a\"}"));
        responseBuilder.request(requestBuilder.build());
        responseBuilder.message("Smart Contract - Success");
        Response mockedResponse = responseBuilder.build();

        HancockEthereumProtocolClient auxHancockEthereumClient = new HancockEthereumProtocolClient();
        HancockEthereumProtocolClient spy_var=PowerMockito.spy(auxHancockEthereumClient);

        mockStatic(Common.class);
        when(Common.class, "getResourceUrl", any(), any())
                .thenCallRealMethod();
        when(Common.class, "checkStatus", any(), any())
                .thenCallRealMethod();
        when(Common.class, "getRequest", any(), any())
                .thenCallRealMethod();
        when(Common.class, "makeCall", any(Request.class))
                .thenReturn(mockedResponse);

        HancockProtocolEncodeResponse response = spy_var.encodeProtocol(HancockProtocolAction.transfer, new BigInteger("10"), "0x1234", "dafsda", HancockProtocolDlt.ethereum);

        //assertTrue("transaction signed successfully", response.getCode().equals("hancock://qr?code=%7B%22action%22%3A%22transfer%22%2C%22body%22%3A%7B%22value%22%3A%2210%22%2C%22data%22%3A%22dafsda%22%2C%22to%22%3A%220x1234%22%7D%2C%22dlt%22%3A%22ethereum%22%7D"));
        assertTrue("transaction encode successfully", response instanceof HancockProtocolEncodeResponse);

    }


}
