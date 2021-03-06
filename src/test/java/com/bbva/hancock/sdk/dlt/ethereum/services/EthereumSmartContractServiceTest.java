package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransactionAdaptResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumWallet;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumCallResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumRegisterResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.models.HancockGenericResponse;
import com.bbva.hancock.sdk.models.TransactionConfig;
import com.bbva.hancock.sdk.util.ValidateParameters;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.web3j.protocol.core.methods.response.AbiDefinition;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@PrepareForTest({ValidateParameters.class, Common.class})
@RunWith(PowerMockRunner.class)
public class EthereumSmartContractServiceTest {

    public static HancockConfig mockedConfig;
    public static TransactionConfig mockedTransactionConfig;
    public static EthereumWallet mockedWallet;
    public static EthereumSmartContractService mockedHancockEthereumClient;
    public static EthereumTransactionService spyTransactionService;
    public static EthereumSmartContractService spy_var;
    public static EthereumTransactionAdaptResponse mockedEthereumAdaptInvoke;
    public static EthereumTransaction mockedEthereumTransaction;
    public static EthereumTransactionResponse mockedEthereumTransactionResponse;

    public static String from;
    public static String to;
    public static String method;
    public static String data;
    public static String addressOrAlias;
    public static ArrayList<AbiDefinition> abi;
    public static ArrayList<String> params;

    @Before
    public void setUp() {

        mockedConfig = new HancockConfig.Builder()
                .withEnv("custom")
                .withNode("http://mock.node.com", 9999)
                .withAdapter("http://mock.adapter.com", "/base", 9999)
                .build();

        mockedTransactionConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        final EthereumTransactionService transactionClient = new EthereumTransactionService(mockedConfig);
        spyTransactionService = PowerMockito.spy(transactionClient);
        mockedHancockEthereumClient = new EthereumSmartContractService(mockedConfig, spyTransactionService);
        spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        mockedWallet = new EthereumWallet("0xmockAddress", "mockPrivateKey", "mockPublicKey");

        final String nonce = String.valueOf(1);
        final String gasPrice = String.valueOf(111);
        final String gasLimit = String.valueOf(222);
        final String value = String.valueOf(333);
        from = mockedWallet.getAddress();
        to = mockedWallet.getAddress();
        method = "mockedMethod";
        addressOrAlias = "mockedAlias";
        abi = new ArrayList<>();
        params = new ArrayList<>();
        params.add("mockedFirtsParam");
        data = "mockedData";

        mockedEthereumTransaction = new EthereumTransaction(from, to, value, data, nonce, gasLimit, gasPrice);
        mockedEthereumAdaptInvoke = new EthereumTransactionAdaptResponse(mockedEthereumTransaction, new HancockGenericResponse(1, "mockedOK"));
        mockedEthereumTransactionResponse = new EthereumTransactionResponse(true);

    }

    @Test
    public void testInvoke() throws Exception {

        PowerMockito.mockStatic(ValidateParameters.class);
        PowerMockito.doNothing().when(ValidateParameters.class, "checkAddress", any(String.class));
        PowerMockito.doNothing().when(ValidateParameters.class, "checkForContent", any(String.class), any(String.class));

        PowerMockito.doReturn(mockedEthereumAdaptInvoke)
                .when(spy_var)
                .adaptInvoke(any(String.class), any(String.class), any(ArrayList.class), any(String.class));

        PowerMockito.doReturn(mockedEthereumTransactionResponse)
                .when(spyTransactionService)
                .send(any(EthereumTransaction.class), any(TransactionConfig.class));


        final EthereumTransactionResponse response = spy_var.invoke(addressOrAlias, method, params, from, mockedTransactionConfig);

        assertTrue("Response is of type TransactionResponse", response instanceof EthereumTransactionResponse);
        verify(spy_var).adaptInvoke(eq(addressOrAlias), eq(method), eq(params), eq(from));
        verify(spyTransactionService).send(eq(mockedEthereumAdaptInvoke.getData()), eq(mockedTransactionConfig));
        assertTrue(response.getSuccess());

    }

    @Test
    public void testInvoke2() throws Exception {

        final TransactionConfig mockedConfig = new TransactionConfig.Builder()
                .withProvider("mockProvider")
                .build();

        PowerMockito.mockStatic(ValidateParameters.class);
        PowerMockito.doNothing().when(ValidateParameters.class, "checkAddress", any(String.class));
        PowerMockito.doNothing().when(ValidateParameters.class, "checkForContent", any(String.class), any(String.class));

        PowerMockito.doReturn(mockedEthereumAdaptInvoke)
                .when(spy_var)
                .adaptInvoke(any(String.class), any(String.class), any(ArrayList.class), any(String.class));

        PowerMockito.doReturn(mockedEthereumTransactionResponse)
                .when(spyTransactionService)
                .send(any(EthereumTransaction.class), any(TransactionConfig.class));

        final EthereumTransactionResponse response = spy_var.invoke(addressOrAlias, method, params, from, mockedConfig);

        assertTrue("Response is of type TransactionResponse", response instanceof EthereumTransactionResponse);
        verify(spy_var).adaptInvoke(eq(addressOrAlias), eq(method), eq(params), eq(from));
        verify(spyTransactionService).send(eq(mockedEthereumAdaptInvoke.getData()), eq(mockedConfig));
        assertTrue(response.getSuccess());

    }

    @Test
    public void testInvoke3() throws Exception {

        final TransactionConfig mockedConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .withProvider("mockProvider")
                .build();

        PowerMockito.mockStatic(ValidateParameters.class);
        PowerMockito.doNothing().when(ValidateParameters.class, "checkAddress", any(String.class));
        PowerMockito.doNothing().when(ValidateParameters.class, "checkForContent", any(String.class), any(String.class));

        PowerMockito.doReturn(mockedEthereumAdaptInvoke)
                .when(spy_var)
                .adaptInvoke(any(String.class), any(String.class), any(ArrayList.class), any(String.class));

        PowerMockito.doReturn(mockedEthereumTransactionResponse)
                .when(spyTransactionService)
                .send(any(EthereumTransaction.class), any(TransactionConfig.class));

        final EthereumTransactionResponse response = spy_var.invoke(addressOrAlias, method, params, from, mockedConfig);

        assertTrue("Response is of type TransactionResponse", response instanceof EthereumTransactionResponse);
        verify(spy_var).adaptInvoke(eq(addressOrAlias), eq(method), eq(params), eq(from));
        verify(spyTransactionService).send(eq(mockedEthereumAdaptInvoke.getData()), eq(mockedConfig));
        assertTrue(response.getSuccess());

    }

    @Test(expected = HancockException.class)
    public void testInvokeException() throws Exception {

        final TransactionConfig mockedConfig = new TransactionConfig();
        spy_var.invoke(addressOrAlias, method, params, from, mockedConfig);

    }

    @Test
    public void testInvokeAbi() throws Exception {

        PowerMockito.mockStatic(ValidateParameters.class);
        PowerMockito.doNothing().when(ValidateParameters.class, "checkAddress", any(String.class));
        PowerMockito.doNothing().when(ValidateParameters.class, "checkForContent", any(String.class), any(String.class));
        final EthereumTransactionAdaptResponse responseModelMock = new EthereumTransactionAdaptResponse(mockedEthereumTransaction, new HancockGenericResponse(1, "mockedOk"));
        final okhttp3.Response responseMock = mock(okhttp3.Response.class);

        mockStatic(Common.class);
        PowerMockito.doReturn(responseMock)
                .when(spy_var)
                .adaptInvokeAbi(any(String.class), any(String.class), any(ArrayList.class), any(String.class), any(String.class), eq(new ArrayList<>()));

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumTransactionAdaptResponse.class))
                .thenReturn(responseModelMock);

        PowerMockito.doReturn(mockedEthereumTransactionResponse)
                .when(spyTransactionService)
                .send(any(EthereumTransaction.class), any(TransactionConfig.class));

        final EthereumTransactionResponse response = spy_var.invokeAbi(addressOrAlias, method, params, from, mockedTransactionConfig, abi);

        assertTrue("Response is of type TransactionResponse", response instanceof EthereumTransactionResponse);
        verify(spy_var).adaptInvokeAbi(eq(addressOrAlias), eq(method), eq(params), eq(from), eq("send"), eq(abi));
        verify(spyTransactionService).send(eq(mockedEthereumAdaptInvoke.getData()), eq(mockedTransactionConfig));
        assertTrue(response.getSuccess());

    }

    @Test(expected = HancockException.class)
    public void testInvokeAbiException() throws Exception {

        final TransactionConfig mockedConfig = new TransactionConfig();
        spy_var.invokeAbi(addressOrAlias, method, params, from, mockedConfig, abi);

    }

    @Test
    public void testCallAbi() throws Exception {

        PowerMockito.mockStatic(ValidateParameters.class);
        PowerMockito.doNothing().when(ValidateParameters.class, "checkAddress", any(String.class));
        PowerMockito.doNothing().when(ValidateParameters.class, "checkForContent", any(String.class), any(String.class));
        final EthereumCallResponse responseModelMock = new EthereumCallResponse(mockedEthereumTransaction, new HancockGenericResponse(1, "mockedOk"));
        final okhttp3.Response responseMock = mock(okhttp3.Response.class);

        mockStatic(Common.class);
        PowerMockito.doReturn(responseMock)
                .when(spy_var)
                .adaptInvokeAbi(any(String.class), any(String.class), any(ArrayList.class), any(String.class), any(String.class), eq(new ArrayList<>()));

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumCallResponse.class))
                .thenReturn(responseModelMock);

        final EthereumCallResponse response = spy_var.callAbi(addressOrAlias, method, params, from, abi);

        assertTrue("Response is of type CallResponse", response instanceof EthereumCallResponse);
        verify(spy_var).adaptInvokeAbi(eq(addressOrAlias), eq(method), eq(params), eq(from), eq("call"), eq(abi));
        assertEquals(response.getResult().getDescription(), "mockedOk");

    }

    @Test(expected = HancockException.class)
    public void testCallAbiException() throws Exception {

        spy_var.callAbi(addressOrAlias, method, params, from, abi);

    }

    @Test
    public void testCall() throws Exception {

        PowerMockito.mockStatic(ValidateParameters.class);
        PowerMockito.doNothing().when(ValidateParameters.class, "checkForContent", any(String.class), any(String.class));
        PowerMockito.doNothing().when(ValidateParameters.class, "checkAddress", any(String.class));

        final okhttp3.Request requestMock = mock(okhttp3.Request.class);
        final okhttp3.Response responseMock = mock(okhttp3.Response.class);
        final EthereumCallResponse responseModelMock = new EthereumCallResponse("mockedCall", new HancockGenericResponse(1, "mockedOk"));

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(RequestBody.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumCallResponse.class))
                .thenReturn(responseModelMock);

        final EthereumCallResponse response = spy_var.call(addressOrAlias, method, params, from);
        assertTrue("Response is of type CallResponse", response instanceof EthereumCallResponse);
        assertEquals(response.getData(), "mockedCall");
        assertEquals(response.getResult().getDescription(), "mockedOk");
    }

    @Test
    public void testRegister() throws Exception {

        PowerMockito.mockStatic(ValidateParameters.class);
        PowerMockito.doNothing().when(ValidateParameters.class, "checkForContent", any(String.class), any(String.class));
        PowerMockito.doNothing().when(ValidateParameters.class, "checkAddress", any(String.class));

        final okhttp3.Request requestMock = mock(okhttp3.Request.class);
        final okhttp3.Response responseMock = mock(okhttp3.Response.class);
        final EthereumRegisterResponse responseModelMock = new EthereumRegisterResponse(new HancockGenericResponse(1, "mockedOk"));

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(RequestBody.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumRegisterResponse.class))
                .thenReturn(responseModelMock);

        final EthereumRegisterResponse response = spy_var.register(addressOrAlias, to, new ArrayList<>());
        assertTrue("Response is of type EthereumRegisterResponse", response instanceof EthereumRegisterResponse);
        assertEquals(response.getResult().getDescription(), "mockedOk");

    }

    @Test
    public void testAdaptInvoke() throws Exception {

        final okhttp3.Request requestMock = mock(okhttp3.Request.class);
        final okhttp3.Response responseMock = mock(okhttp3.Response.class);
        final EthereumTransactionAdaptResponse responseModelMock = new EthereumTransactionAdaptResponse(mockedEthereumTransaction, new HancockGenericResponse(1, "mockedOk"));

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(RequestBody.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumTransactionAdaptResponse.class))
                .thenReturn(responseModelMock);

        final EthereumTransactionAdaptResponse response = spy_var.adaptInvoke(addressOrAlias, method, params, from);
        assertTrue("Response is of type EthereumTransactionAdaptResponse", response instanceof EthereumTransactionAdaptResponse);
        assertEquals(response.getData().getFrom(), from);
        assertEquals(response.getData().getTo(), to);
        assertEquals(response.getData().getData(), data);

    }

    @Test
    public void testAdaptInvokeAbi() throws Exception {

        final okhttp3.Request requestMock = mock(okhttp3.Request.class);
        final okhttp3.Response responseMock = mock(okhttp3.Response.class);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(RequestBody.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        final Response response = spy_var.adaptInvokeAbi(addressOrAlias, method, params, from, "call", abi);
        assertTrue("Response is of type Response", response instanceof Response);

    }

}
