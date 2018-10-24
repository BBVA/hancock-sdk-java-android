package com.bbva.hancock.sdk.dlt.ethereum.services;


import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumSmartContractRetrieveResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumWallet;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumContractInstance;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenInstance;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.allowance.EthereumTokenAllowanceRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.approve.EthereumTokenApproveRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.balance.EthereumTokenBalance;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.balance.EthereumTokenBalanceResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata.EthereumTokenMetadata;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata.EthereumTokenMetadataResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.register.EthereumTokenRegisterRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.register.EthereumTokenRegisterResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.transfer.EthereumTokenTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.transferFrom.EthereumTokenTransferFromRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.models.HancockGenericResponse;
import com.bbva.hancock.sdk.models.TransactionConfig;
import com.bbva.hancock.sdk.util.ValidateParameters;
import okhttp3.RequestBody;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;
@PowerMockIgnore("javax.net.ssl.*")
//@RunWith(MockitoJUnitRunner.class)
@RunWith(PowerMockRunner.class)
public class EthereumTokenServiceTest {

    public static HancockConfig mockedConfig;
    public static EthereumWallet mockedWallet;
    public static EthereumTransaction mockedEthereumRawTransaction;
    public static EthereumTransferRequest mockedEthereumTransferRequest;
    public static EthereumTokenRequest mockedEthereumTokenRequest;
    public static EthereumTokenTransferRequest mockedEthereumTokenTransferRequest;
    public static EthereumTokenTransferFromRequest mockedEthereumTokenTransferFromRequest;
    public static EthereumTokenAllowanceRequest mockedEthereumTokenAllowanceRequest;
    public static EthereumTokenApproveRequest mockedEthereumTokenApproveRequest;
    public static EthereumTokenService mockedHancockEthereumClient;
    public static EthereumTransactionResponse mockedEthereumTransactionResponse;
    public static EthereumTransactionService spyTransactionService;

    @BeforeClass
    public static void setUp() throws Exception{

        mockedConfig = new HancockConfig.Builder()
                .withEnv("custom")
                .withNode("http://mock.node.com", 9999)
                .withAdapter("http://mock.adapter.com", "/base", 9999)
                .build();

        EthereumTransactionService transactionClient = new EthereumTransactionService(mockedConfig);
        spyTransactionService = PowerMockito.spy(transactionClient);
        mockedHancockEthereumClient = new EthereumTokenService(mockedConfig, spyTransactionService);
        mockedWallet = new EthereumWallet("0xmockAddress","mockPrivateKey","mockPublicKey");

        BigInteger nonce = BigInteger.valueOf(1);
        BigInteger gasPrice = BigInteger.valueOf(111);
        BigInteger gasLimit = BigInteger.valueOf(222);
        BigInteger value = BigInteger.valueOf(333);
        String from = mockedWallet.getAddress();
        String sender = mockedWallet.getAddress();
        String to = mockedWallet.getAddress();
        String data = "0xwhatever";
        String addressOrAlias = "mockedAlias";
        String tokenOwner = "0xmockedtokenOwner";
        String spender = "0xmockedSpender";
        String mockedEncodeUrl = "tokenTransfer";

        mockedEthereumTokenTransferRequest = new EthereumTokenTransferRequest(from, to, value.toString(), addressOrAlias);
        mockedEthereumTokenTransferFromRequest = new EthereumTokenTransferFromRequest(from, sender, to, value.toString(), addressOrAlias);
        mockedEthereumTransferRequest = new EthereumTransferRequest(from, to, value.toString(), data);
        mockedEthereumTokenRequest = new EthereumTokenRequest(addressOrAlias, mockedEncodeUrl);
        mockedEthereumRawTransaction = new EthereumTransaction(from, to, value.toString(), nonce.toString(), gasLimit.toString(), gasPrice.toString());
        mockedEthereumTokenAllowanceRequest = new EthereumTokenAllowanceRequest(from, tokenOwner, spender, addressOrAlias);
        mockedEthereumTokenApproveRequest = new EthereumTokenApproveRequest(from, spender, data, addressOrAlias);
        mockedEthereumTransactionResponse = new EthereumTransactionResponse(true);

    }

    @PrepareForTest({ ValidateParameters.class, Common.class})
    @Test public void testGetTokenBalance() throws Exception {

        PowerMockito.mockStatic(ValidateParameters.class);
        PowerMockito.doNothing().when(ValidateParameters.class, "checkAddress", any(String.class));
        PowerMockito.doNothing().when(ValidateParameters.class, "checkForContent", any(String.class) , any(String.class));

        EthereumTokenBalance aux = mock(EthereumTokenBalance.class);
        EthereumTokenBalanceResponse responseModel = mock(EthereumTokenBalanceResponse.class);
        okhttp3.Response responseMock = mock(okhttp3.Response.class);
        okhttp3.Request requestMock = mock(okhttp3.Request.class);
        EthereumTokenService spy_var = PowerMockito.spy(mockedHancockEthereumClient);
        HancockGenericResponse mockResult = new HancockGenericResponse(200, "success");

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumTokenBalanceResponse.class))
                .thenReturn(responseModel);

        when(responseModel.getTokenBalance()).thenReturn(aux);
        when(aux.getBalance()).thenReturn(BigInteger.valueOf(0));
        when(responseModel.getResult()).thenReturn(mockResult);

        EthereumTokenBalance balance = spy_var.getBalance("mockAlias","mockAddress");
        assertTrue("Wallet should have a Balance", balance instanceof EthereumTokenBalance);
        assertEquals(balance.getBalance(), BigInteger.valueOf(0));

    }

    @PrepareForTest({ ValidateParameters.class, Common.class})
    @Test public void testGetTokenMetadata() throws Exception {

        PowerMockito.mockStatic(ValidateParameters.class);
        PowerMockito.doNothing().when(ValidateParameters.class, "checkForContent", any(String.class) , any(String.class));

        EthereumTokenMetadata aux = mock(EthereumTokenMetadata.class);
        EthereumTokenMetadataResponse responseModel = mock(EthereumTokenMetadataResponse.class);
        HancockGenericResponse mockResult = new HancockGenericResponse(200, "success");

        okhttp3.Response responseMock = mock(okhttp3.Response.class);
        okhttp3.Request requestMock = mock(okhttp3.Request.class);

        EthereumTokenService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumTokenMetadataResponse.class))
                .thenReturn(responseModel);

        when(responseModel.getTokenMetadata()).thenReturn(aux);
        when(aux.getName()).thenReturn("mockedName");
        when(aux.getSymbol()).thenReturn("mockedSymbol");
        when(aux.getDecimals()).thenReturn(10);
        when(aux.getTotalSupply()).thenReturn(10000);
        when(responseModel.getResult()).thenReturn(mockResult);

        EthereumTokenMetadata metadata = spy_var.getMetadata("mockAlias");

        assertTrue("Wallet should have a Balance", metadata instanceof EthereumTokenMetadata);
        assertEquals(metadata.getName(), "mockedName");
        assertEquals(metadata.getSymbol(), "mockedSymbol");
        assertEquals(metadata.getDecimals(), Integer.valueOf(10));
        assertEquals(metadata.getTotalSupply(), Integer.valueOf(10000));
    }

    @PrepareForTest({ Common.class})
    @Test public void testGetAllTokens() throws Exception {

        EthereumTokenInstance instanceAux = new EthereumTokenInstance("erc20", "TKN", "mockedAddress", "TKN", "tkn", 10, 10000000);
        ArrayList<EthereumTokenInstance> list = new ArrayList<>();
        list.add(instanceAux);
        HancockGenericResponse result = mock(HancockGenericResponse.class);
        EthereumSmartContractRetrieveResponse responseModel = new EthereumSmartContractRetrieveResponse(list, result);

        okhttp3.Response responseMock = mock(okhttp3.Response.class);
        okhttp3.Request requestMock = mock(okhttp3.Request.class);

        EthereumTokenService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumSmartContractRetrieveResponse.class))
                .thenReturn(responseModel);

        ArrayList<EthereumTokenInstance> responseList = spy_var.getAllTokens();

        assertEquals(responseList.size(), 1);
        assertEquals(responseList.get(0).getAbiName(), "erc20");
        assertEquals(responseList.get(0).getAddress(), "mockedAddress");
        assertEquals(responseList.get(0).getAlias(), "TKN");

    }

    @PrepareForTest({ ValidateParameters.class, Common.class})
    @Test public void testTokenTransfer() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        txConfig.setSendLocally(true);

        EthereumTokenService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        PowerMockito.doReturn(mock(EthereumTransaction.class))
                .when(spy_var)
                .adaptTransfer(any(EthereumTokenTransferRequest.class));

        PowerMockito.doReturn(mockedEthereumTransactionResponse)
                .when(spyTransactionService)
                .send(any(EthereumTransaction.class), any(TransactionConfig.class));

        EthereumTransactionResponse mockResult = spy_var.transfer(mockedEthereumTokenTransferRequest, txConfig);

        assertEquals(mockResult, mockedEthereumTransactionResponse);
        verify(spy_var).adaptTransfer(eq(mockedEthereumTokenTransferRequest));
        verify(spyTransactionService).send(any(EthereumTransaction.class), eq(txConfig));
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);

    }

    @PrepareForTest({ ValidateParameters.class, Common.class})
    @Test public void testTokenTransferFrom() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withSendLocally(true)
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        EthereumTokenService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        PowerMockito.doReturn(mock(EthereumTransaction.class))
                .when(spy_var)
                .adaptTransfer(any(EthereumTokenTransferFromRequest.class));

        PowerMockito.doReturn(mockedEthereumTransactionResponse)
                .when(spyTransactionService)
                .send(any(EthereumTransaction.class), any(TransactionConfig.class));

        EthereumTransactionResponse mockResult = spy_var.transferFrom(mockedEthereumTokenTransferFromRequest, txConfig);

        verify(spy_var).adaptTransfer(eq(mockedEthereumTokenTransferFromRequest));
        verify(spyTransactionService).send(any(EthereumTransaction.class), eq(txConfig));
        assertEquals(mockResult, mockedEthereumTransactionResponse);
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);

    }

    @PrepareForTest({ ValidateParameters.class, Common.class})
    @Test public void testTokenAllowance() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        EthereumTokenService spy_var = PowerMockito.spy(mockedHancockEthereumClient);


        PowerMockito.doReturn(mock(EthereumTransaction.class))
                .when(spy_var)
                .adaptTransfer(any(EthereumTokenAllowanceRequest.class));

        PowerMockito.doReturn(mockedEthereumTransactionResponse)
                .when(spyTransactionService)
                .send(any(EthereumTransaction.class), any(TransactionConfig.class));

        EthereumTransactionResponse mockResult = spy_var.allowance(mockedEthereumTokenAllowanceRequest, txConfig);

        verify(spy_var).adaptTransfer(eq(mockedEthereumTokenAllowanceRequest));
        verify(spyTransactionService).send(any(EthereumTransaction.class), eq(txConfig));

        assertEquals(mockResult, mockedEthereumTransactionResponse);
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);

    }

    @PrepareForTest({ ValidateParameters.class, Common.class})
    @Test public void testTokenApprove() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        EthereumTokenService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        PowerMockito.doReturn(mock(EthereumTransaction.class))
                .when(spy_var)
                .adaptTransfer(any(EthereumTokenApproveRequest.class));

        PowerMockito.doReturn(mockedEthereumTransactionResponse)
                .when(spyTransactionService)
                .send(any(EthereumTransaction.class), any(TransactionConfig.class));

        EthereumTransactionResponse mockResult = spy_var.approve(mockedEthereumTokenApproveRequest, txConfig);

        verify(spy_var).adaptTransfer(eq(mockedEthereumTokenApproveRequest));
        verify(spyTransactionService).send(any(EthereumTransaction.class), eq(txConfig));

        assertEquals(mockResult, mockedEthereumTransactionResponse);
        assertTrue("transaction send and signed successfully", mockResult instanceof EthereumTransactionResponse);

    }

    @PrepareForTest({ ValidateParameters.class, Common.class})
    @Test public void testTokenRegister() throws Exception {

        PowerMockito.mockStatic(ValidateParameters.class);
        PowerMockito.doNothing().when(ValidateParameters.class, "checkAddress", any(String.class));
        PowerMockito.doNothing().when(ValidateParameters.class, "checkForContent", any(String.class) , any(String.class));
        HancockGenericResponse mockResult = new HancockGenericResponse(200, "success");
        
        okhttp3.Request requestMock = mock(okhttp3.Request.class);
        okhttp3.Response responseMock = mock(okhttp3.Response.class);
        EthereumTokenRegisterResponse responseModel = new EthereumTokenRegisterResponse(mockResult);
        EthereumTokenService spy_var = PowerMockito.spy(mockedHancockEthereumClient);

        mockStatic(Common.class);
        PowerMockito.when(Common.class, "getRequest", any(String.class), any(RequestBody.class))
                .thenReturn(requestMock);

        PowerMockito.when(Common.class, "getResourceUrl", any(HancockConfig.class), any(String.class))
                .thenReturn("mockUrl");

        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
                .thenReturn(responseMock);

        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumTokenRegisterResponse.class))
                .thenReturn(responseModel);

        EthereumTokenRegisterResponse resultFinal = spy_var.register("mockAlias", "mockaddress");
        
        assertTrue("tokenRegister called successfully", resultFinal instanceof EthereumTokenRegisterResponse);
        assertEquals(responseModel, resultFinal);

    }

//    @PrepareForTest({ ValidateParameters.class, Common.class})
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
//        EthereumTokenService spy_token_var = PowerMockito.spy(mockedHancockEthereumClient);
//
//        mockStatic(Common.class);
//        PowerMockito.when(Common.class, "getRequest", any(String.class), any(RequestBody.class))
//                .thenReturn(requestMock);
//
//        PowerMockito.when(Common.class, "getResourceUrl", any(HancockConfig.class), any(String.class))
//                .thenReturn("mockUrl");
//
//        PowerMockito.when(Common.class, "makeCall", any(okhttp3.Request.class))
//                .thenReturn(responseMock);
//
//        PowerMockito.when(Common.class, "checkStatus", any(okhttp3.Response.class), eq(EthereumTransaction.class))
//                .thenReturn(responseModel);
//
//        PowerMockito.when(responseModel.getNonce()).thenReturn(nonce);
//        PowerMockito.when(responseModel.getGasPrice()).thenReturn(gasPrice);
//        PowerMockito.when(responseModel.getGas()).thenReturn(gasLimit);
//        PowerMockito.when(responseModel.getTo()).thenReturn(to);
//        PowerMockito.when(responseModel.getValue()).thenReturn(value);
//        PowerMockito.when(responseModel.getData()).thenReturn(data);
//        PowerMockito.whenNew(EthereumTransaction.class).withAnyArguments().thenReturn(mockedEthereumRawTransaction);
//        PowerMockito.doReturn("mockedUrl").when(spy_token_var).getTransferUrl(any(EthereumTokenRequest.class));
//
//        EthereumTransaction transfer = spy_token_var.adaptTransfer(mockedEthereumTokenRequest);
//
//        assertTrue("Wallet should have a Balance", transfer instanceof EthereumTransaction);
//        assertEquals(transfer.getGasPrice(), mockedEthereumRawTransaction.getGasPrice());
//        assertEquals(transfer.getTo(), mockedEthereumRawTransaction.getTo());
//        assertEquals(transfer.getValue(), mockedEthereumRawTransaction.getValue());
//        assertEquals(transfer.getNonce(), mockedEthereumRawTransaction.getNonce());
//
//    }

    @PrepareForTest({ ValidateParameters.class, Common.class})
    @Test public void testGetTransferUrl() throws Exception {

        EthereumTokenService spy_var = PowerMockito.spy(mockedHancockEthereumClient);
        mockStatic(Common.class);

        PowerMockito.when(Common.class, "getResourceUrl", any(HancockConfig.class), eq(mockedEthereumTokenRequest.getEncodeUrl()))
                .thenReturn("urlTransfer");

        String mockResult = spy_var.getTransferUrl(mockedEthereumTokenRequest);

        assertEquals(mockResult, "urlTransfer");
        assertTrue("getTransferUrl correctly", mockResult instanceof String);


        mockedEthereumTokenRequest.setEncodeUrl("tokenTransferFrom");

        PowerMockito.when(Common.class, "getResourceUrl", any(HancockConfig.class), eq(mockedEthereumTokenRequest.getEncodeUrl()))
                .thenReturn("urlTokenTransfer");

        String mockResultToken = spy_var.getTransferUrl(mockedEthereumTokenRequest);

        assertEquals(mockResultToken, "urlTokenTransfer");
        assertTrue("getTokenTransferUrl correctly", mockResultToken instanceof String);

    }

}
