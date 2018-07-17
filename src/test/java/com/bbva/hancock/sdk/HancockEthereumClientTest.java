package com.bbva.hancock.sdk;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.config.HancockConfigAdapter;
import com.bbva.hancock.sdk.config.HancockConfigNode;

import com.bbva.hancock.sdk.models.EthereumTokenTransferRequest;
import com.bbva.hancock.sdk.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.models.EthereumTransferResponse;
import com.bbva.hancock.sdk.models.GetBalanceResponse;
import com.bbva.hancock.sdk.models.HancockProtocolAction;
import com.bbva.hancock.sdk.models.HancockProtocolDecodeRequest;
import com.bbva.hancock.sdk.models.HancockProtocolDecodeResponse;
import com.bbva.hancock.sdk.models.HancockProtocolDlt;
import com.bbva.hancock.sdk.models.HancockProtocolEncodeRequest;
import com.bbva.hancock.sdk.models.HancockProtocolEncodeResponse;
import com.bbva.hancock.sdk.models.TransactionConfig;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import org.junit.Test;
import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.stubbing.Answer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;
import org.junit.BeforeClass;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.io.IOException;
import java.math.BigInteger;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@PowerMockIgnore("javax.net.ssl.*")
//@RunWith(MockitoJUnitRunner.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest({TransactionEncoder.class,Credentials.class,EthereumRawTransaction.class,RawTransaction.class,Web3jFactory.class,
  OkHttpClient.class,Call.class,okhttp3.Response.class,okhttp3.Request.class,GetBalanceResponse.class,HancockConfig.class})
public class HancockEthereumClientTest {
  
    public static HancockConfig mockedConfig;
    public static EthereumWallet mockedWallet;
    public static EthereumRawTransaction mockedEthereumRawTransaction;
    public static EthereumTransferRequest mockedEthereumTransferRequest;
    public static EthereumTokenTransferRequest mockedEthereumTokenTransferRequest;
    public static HancockEthereumClient mockedHancockEthereumClient;

    @BeforeClass
    public static void setUp() throws Exception{

      mockedConfig = new HancockConfig.Builder()
          .withEnv("custom")
          .withNode("http://mock.node.com", 9999)
          .withAdapter("http://mock.adapter.com", "/base", 9999)
          .build();
      mockedHancockEthereumClient = new HancockEthereumClient();
      mockedWallet = new EthereumWallet("0xmockAddress","mockPrivateKey","mockPublicKey");
      
      BigInteger nonce = BigInteger.valueOf(1);
      BigInteger gasPrice = BigInteger.valueOf(111);
      BigInteger gasLimit = BigInteger.valueOf(222);
      BigInteger value = BigInteger.valueOf(333);
      String from = mockedWallet.getAddress();
      String to = mockedWallet.getAddress();
      String data = "0xwhatever";
      String addressOrAlias = "mockedAlias";
      
      mockedEthereumTransferRequest = new EthereumTransferRequest(from, to, value.toString(), data);
      mockedEthereumTokenTransferRequest = new EthereumTokenTransferRequest(from, to, value.toString(), addressOrAlias);
      mockedEthereumRawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value);
  
    }
  
    @Test public void testDumb()  {

        String test = "TEST";
        assertEquals(test, "TEST");

    }
    
    @Test public void testConfig() throws Exception {
        HancockConfig.Builder builder = new HancockConfig.Builder();
        //builder.withAdapter("host", "base", 80);
        HancockConfig.Builder spy_var=PowerMockito.spy(builder);
        PowerMockito.doReturn(mockedConfig).when(spy_var).build();        
        HancockConfig result = spy_var.build(); 
        System.out.println("config  "+result.getEnv());
        assertTrue("Config OK", result instanceof HancockConfig);
    }

    @Test public void testConfigInstantiation() throws Exception {


        assertEquals(mockedConfig.getEnv(), "custom");

        HancockConfigNode node = mockedConfig.getNode();
        assertEquals(node.getHost(), "http://mock.node.com");
        assertEquals(node.getPort(), 9999);

        HancockConfigAdapter adapter = mockedConfig.getAdapter();
        assertEquals(adapter.getHost(), "http://mock.adapter.com");
        assertEquals(adapter.getBase(), "/base");
        assertEquals(adapter.getPort(), 9999);
        assertEquals(adapter.getResources().get("balance"), "/ethereum/balance/__ADDRESS__");

    }

    @Test public void testGenerateWallet() throws Exception {

        assertTrue("Wallet should have an address", mockedWallet.getAddress() instanceof String);
        assertTrue("Wallet should have a publicKey", mockedWallet.getPublicKey() instanceof String);
        assertTrue("Wallet should have a privateKey", mockedWallet.getPrivateKey() instanceof String);
        
        assertEquals(mockedWallet.getAddress(), "0xmockAddress");
        assertEquals(mockedWallet.getPrivateKey(), "mockPrivateKey");
        assertEquals(mockedWallet.getPublicKey(), "mockPublicKey");

    }

    @Test public void testGetBalance() throws Exception {
      
        GetBalanceResponse responseModel= mock(GetBalanceResponse.class);
        okhttp3.Response responseMock= mock(okhttp3.Response.class);
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
        PowerMockito.doReturn(responseMock).when(spy_var).makeCall(any(okhttp3.Request.class));
        PowerMockito.doReturn(responseModel).when(spy_var).checkStatus(any(okhttp3.Response.class), eq(GetBalanceResponse.class)); 
        when(responseModel.getBalance()).thenReturn("0");
        
        BigInteger balance = spy_var.getBalance("0xmockAddress");
        System.out.println("balance  "+balance);
        assertTrue("Wallet should have a Balance", balance instanceof BigInteger);
        assertEquals(balance, BigInteger.valueOf(0));

    }
    
    @Test public void testSignTransaction() throws Exception {
      
      
        PowerMockito.mockStatic(Credentials.class);
        when(Credentials.create("mockPrivateKey")).thenReturn(mock(Credentials.class));

        PowerMockito.mockStatic(RawTransaction.class);
        EthereumRawTransaction mockEthereumRawTransaction = mock(EthereumRawTransaction.class);
        when(mockEthereumRawTransaction.getWeb3Instance()).thenReturn(mock(RawTransaction.class));

        byte[] aux = "mockedsignedTransaction".getBytes();

        PowerMockito.mockStatic(TransactionEncoder.class);
        when(TransactionEncoder.signMessage(any(RawTransaction.class), any(Credentials.class))).thenReturn(aux);        

        String signedTransaction = mockedHancockEthereumClient.signTransaction(mockEthereumRawTransaction,"mockPrivateKey");
        System.out.println("sign  "+signedTransaction);
        assertTrue("transaction signed successfully",  signedTransaction instanceof String);
        assertEquals(signedTransaction,  Numeric.toHexString(aux));

    }
    
    @Test public void testCreateRawTransaction() throws Exception {

        assertTrue("RawTransaction is well constructed ", mockedEthereumRawTransaction instanceof EthereumRawTransaction);
        assertTrue("RawTransaction web3 instance is well constructed ", mockedEthereumRawTransaction.getWeb3Instance() instanceof RawTransaction);
        assertTrue("RawTransaction has nonce ", mockedEthereumRawTransaction.getNonce() instanceof BigInteger);
        assertTrue("RawTransaction has gasPrice ", mockedEthereumRawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has gasLimit ", mockedEthereumRawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has to ", mockedEthereumRawTransaction.getTo() instanceof String);
        assertTrue("RawTransaction has value ", mockedEthereumRawTransaction.getValue() instanceof BigInteger);
        assertTrue("RawTransaction has value ", mockedEthereumRawTransaction.getData() instanceof String);

    }

     @Test public void testAdaptTransfer() throws Exception {
     
       BigInteger nonce = BigInteger.valueOf(1);
       BigInteger gasPrice = BigInteger.valueOf(111);
       BigInteger gasLimit = BigInteger.valueOf(222);
       BigInteger value = BigInteger.valueOf(333);
       String to = mockedWallet.getAddress();
       String data = "0xwhatever";
       
       EthereumTransferResponse responseModel= mock(EthereumTransferResponse.class);
       okhttp3.Request requestMock= mock(okhttp3.Request.class);
       okhttp3.Response responseMock= mock(okhttp3.Response.class);
       HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
       HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
       PowerMockito.doReturn("mockUrl").when(spy_var).getResourceUrl("transfer");
       PowerMockito.doReturn(requestMock).when(spy_var).getRequest(any(String.class), any(okhttp3.RequestBody.class));
       PowerMockito.doReturn(responseMock).when(spy_var).makeCall(any(okhttp3.Request.class));
       PowerMockito.doReturn(responseModel).when(spy_var).checkStatus(any(okhttp3.Response.class), eq(EthereumTransferResponse.class)); 
       PowerMockito.when(responseModel.getNonce()).thenReturn(nonce);
       PowerMockito.when(responseModel.getGasPrice()).thenReturn(gasPrice);
       PowerMockito.when(responseModel.getGas()).thenReturn(gasLimit);
       PowerMockito.when(responseModel.getTo()).thenReturn(to);
       PowerMockito.when(responseModel.getValue()).thenReturn(value);
       PowerMockito.when(responseModel.getData()).thenReturn(data);
       PowerMockito.whenNew(EthereumRawTransaction.class).withAnyArguments().thenReturn(mockedEthereumRawTransaction);
       
       EthereumRawTransaction transfer = spy_var.adaptTransfer(mockedEthereumTransferRequest, "mockUrl");
       System.out.println("Adapter  "+ mockedEthereumRawTransaction.getNonce());
       assertTrue("Wallet should have a Balance", transfer instanceof EthereumRawTransaction);
       assertEquals(transfer.getGasPrice(), mockedEthereumRawTransaction.getGasPrice());
       assertEquals(transfer.getTo(), mockedEthereumRawTransaction.getTo());
       assertEquals(transfer.getValue(), mockedEthereumRawTransaction.getValue());
       assertEquals(transfer.getNonce(), mockedEthereumRawTransaction.getNonce());
       
     }

     @Test public void testsendSignedTransaction() throws Exception {

       HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
       HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);       
       PowerMockito.doReturn("mockSignedTransaction").when(spy_var).sendSignedTransaction(any(String.class), any(boolean.class), any(String.class)); 
       
       String mockResult = spy_var.sendSignedTransaction("mockSignedTransaction", true);
       System.out.println("sendSigned: "+ mockResult);
       assertEquals(mockResult, "mockSignedTransaction");
       assertTrue("transaction send and signed successfully", mockResult instanceof String);

     }
     
     @Test public void testsendSignedTransactionWith() throws Exception {

       HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
       HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);       
       PowerMockito.doReturn("mockSignedTransactionLocal").when(spy_var).sendSignedTransactionLocally(any(String.class), any(String.class));
       PowerMockito.doReturn("mockSignedTransactionRemote").when(spy_var).sendSignedTransactionRemotely(any(String.class), any(String.class)); 
       
       String mockResulttrue = spy_var.sendSignedTransaction("mockSignedTransaction", true, "mockurl");
       String mockResultfalse = spy_var.sendSignedTransaction("mockSignedTransaction", false, "mockurl");
       System.out.println("sendSignedWith: "+ mockResulttrue + " / " + mockResultfalse);
       assertEquals(mockResulttrue, "mockSignedTransactionLocal");
       assertTrue("transaction send and signed successfully local", mockResulttrue instanceof String);
       assertEquals(mockResultfalse, "mockSignedTransactionRemote");
       assertTrue("transaction send and signed successfully remote", mockResultfalse instanceof String);

     }
     
     @Test public void testsendSignedTransactionLocal() throws Exception {

//       Web3j web3j = mock(Web3j.class);    
//       Web3jFactory web = new Web3jFactory();
//       doReturn(web3j).when(web).build(new HttpService("http://mock.node.com"));
//       HancockEthereumClient testHancockEthereumClient = new HancockEthereumClient();
//       HancockEthereumClient spytest_var=PowerMockito.spy(testHancockEthereumClient);            
//       EthSendTransaction responseModel= mock(EthSendTransaction.class);
//       doReturn(responseModel).when(web3j).ethSendRawTransaction(any(String.class)).sendAsync().get();
//       doReturn("mockhash").when(responseModel).getTransactionHash();
       
       HancockEthereumClient testHancockEthereumClient = new HancockEthereumClient();
       HancockEthereumClient spytest_var=PowerMockito.spy(testHancockEthereumClient);
       PowerMockito.doReturn("mockSignedTransactionLocal").when(spytest_var).sendSignedTransactionLocally(any(String.class), any(String.class));
       
       String mockResult = spytest_var.sendSignedTransactionLocally("mockSignedTransaction");
       System.out.println("sendSignedLocal: "+ mockResult );
       assertEquals(mockResult, "mockSignedTransactionLocal");
       assertTrue("transaction send and signed successfully local", mockResult instanceof String);

     }
     
     @Test (expected = Exception.class)
     public void testsendSignedTransactionRemote() throws Exception {
     
     HancockEthereumClient testHancockEthereumClient = new HancockEthereumClient();
     HancockEthereumClient spytest_var=PowerMockito.spy(testHancockEthereumClient);
     //PowerMockito.doThrow(new Exception("Not implemented")).when(spytest_var).sendSignedTransactionRemotely(any(String.class), any(String.class));
     
     String mockResult = spytest_var.sendSignedTransactionRemotely("signedTransaction", "backUrl");

   }
     
     @Test public void testTransfer() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        txConfig.setSendLocally(true);
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
        PowerMockito.doReturn(mock(EthereumRawTransaction.class)).when(spy_var).adaptTransfer(any(EthereumTransferRequest.class), any(String.class));
        PowerMockito.doReturn("mockSignedTransaction").when(spy_var).sendTransfer(any(TransactionConfig.class), any(EthereumRawTransaction.class));

        String mockResult = spy_var.transfer(mockedEthereumTransferRequest, txConfig);
        System.out.println("transfer: "+ mockResult);
        assertEquals(mockResult, "mockSignedTransaction");
        assertTrue("transaction send and signed successfully", mockResult instanceof String);

//       String mockResultLocal = spy_var.transfer(mockedEthereumTransferRequest, txConfig);
//       System.out.println("transfer local: "+ mockResultLocal);
//       assertEquals(mockResultLocal, "mockSignedTransaction");
//       assertTrue("transaction send and signed successfully", mockResultLocal instanceof String);

    }

    @Test public void testTokenTransfer() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        txConfig.setSendLocally(true);
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
        PowerMockito.doReturn(mock(EthereumRawTransaction.class)).when(spy_var).adaptTransfer(any(EthereumTokenTransferRequest.class), any(String.class));
        PowerMockito.doReturn("mockSignedTransaction").when(spy_var).sendTransfer(any(TransactionConfig.class), any(EthereumRawTransaction.class));

        String mockResult = spy_var.tokenTransfer(mockedEthereumTokenTransferRequest, txConfig);
        System.out.println("transfer: "+ mockResult);
        assertEquals(mockResult, "mockSignedTransaction");
        assertTrue("transaction send and signed successfully", mockResult instanceof String);

//       String mockResultLocal = spy_var.transfer(mockedEthereumTransferRequest, txConfig);
//       System.out.println("transfer local: "+ mockResultLocal);
//       assertEquals(mockResultLocal, "mockSignedTransaction");
//       assertTrue("transaction send and signed successfully", mockResultLocal instanceof String);

    }

    @Test public void testSendTransfer() throws Exception {

        TransactionConfig txConfig = new TransactionConfig.Builder()
                .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
                .build();

        txConfig.setSendLocally(true);
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
        PowerMockito.doReturn("mockSigned").when(spy_var).signTransaction(any(EthereumRawTransaction.class),eq("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8"));
        PowerMockito.doReturn("mockSignedTransaction").when(spy_var).sendSignedTransaction(eq("mockSigned"), any(boolean.class), any(String.class));

        String mockResult = spy_var.sendTransfer(txConfig, mock(EthereumRawTransaction.class));
        System.out.println("transfer: "+ mockResult);
        assertEquals(mockResult, "mockSignedTransaction");
        assertTrue("transaction send and signed successfully", mockResult instanceof String);

//       String mockResultLocal = spy_var.transfer(mockedEthereumTransferRequest, txConfig);
//       System.out.println("transfer local: "+ mockResultLocal);
//       assertEquals(mockResultLocal, "mockSignedTransaction");
//       assertTrue("transaction send and signed successfully", mockResultLocal instanceof String);

    }
  
    @Test public void testDecodeProtocol() throws Exception {

//        OkHttpClient httpClient = new OkHttpClient();
//        Call response = mock(Call.class);
//        PowerMockito.whenNew(OkHttpClient.class).withAnyArguments().thenReturn(httpClient);
//        OkHttpClient spy_http=PowerMockito.spy(httpClient);
//        Call spy_call=PowerMockito.spy(response);
//        PowerMockito.doReturn(spy_call).when(spy_http).newCall(any(okhttp3.Request.class));
//        PowerMockito.doReturn(mock(okhttp3.Response.class)).when(spy_call).execute();

        okhttp3.Request requestMock= mock(okhttp3.Request.class);
        okhttp3.Response responseMock= mock(okhttp3.Response.class);
        HancockProtocolDecodeResponse responseModel= mock(HancockProtocolDecodeResponse.class);
        HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
        HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
        PowerMockito.doReturn("mockUrl").when(spy_var).getResourceUrl("decode");
        PowerMockito.doReturn(requestMock).when(spy_var).getRequest(any(String.class), any(okhttp3.RequestBody.class));
        PowerMockito.doReturn(responseMock).when(spy_var).makeCall(any(okhttp3.Request.class));
        PowerMockito.doReturn(responseModel).when(spy_var).checkStatus(any(okhttp3.Response.class), eq(HancockProtocolDecodeResponse.class));

        HancockProtocolDecodeResponse mockResult = spy_var.decodeProtocol("mockedCode");
        System.out.println("transaction decode: ");
        assertTrue("transaction decode successfully", mockResult instanceof HancockProtocolDecodeResponse);


    }

    @Test public void testEncodeProtocol() throws Exception {

      okhttp3.Request requestMock= mock(okhttp3.Request.class);
      okhttp3.Response responseMock= mock(okhttp3.Response.class);
      HancockProtocolEncodeResponse responseModel= mock(HancockProtocolEncodeResponse.class);
      HancockEthereumClient auxHancockEthereumClient = new HancockEthereumClient();
      PowerMockito.whenNew(HancockProtocolEncodeRequest.class).withAnyArguments().thenReturn(mock(HancockProtocolEncodeRequest.class));

      HancockEthereumClient spy_var=PowerMockito.spy(auxHancockEthereumClient);
      PowerMockito.doReturn("mockUrl").when(spy_var).getResourceUrl("encode");
      PowerMockito.doReturn(requestMock).when(spy_var).getRequest(any(String.class), any(okhttp3.RequestBody.class));
      PowerMockito.doReturn(responseMock).when(spy_var).makeCall(any(okhttp3.Request.class));
      PowerMockito.doReturn(responseModel).when(spy_var).checkStatus(any(okhttp3.Response.class), eq(HancockProtocolEncodeResponse.class)); 

      HancockProtocolEncodeResponse mockResult = spy_var.encodeProtocol(HancockProtocolAction.transfer, new BigInteger("10"), "0x1234", "dafsda", HancockProtocolDlt.ethereum);
      
      assertTrue("transaction encode successfully", mockResult instanceof HancockProtocolEncodeResponse);
      System.out.println("transaction encode: ");
      
    }
}
