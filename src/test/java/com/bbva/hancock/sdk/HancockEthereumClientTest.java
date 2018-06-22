package com.bbva.hancock.sdk;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.config.HancockConfigAdapter;
import com.bbva.hancock.sdk.config.HancockConfigNode;

import com.bbva.hancock.sdk.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.models.HancockProtocolAction;
import com.bbva.hancock.sdk.models.HancockProtocolDecodeResponse;
import com.bbva.hancock.sdk.models.HancockProtocolDlt;
import com.bbva.hancock.sdk.models.HancockProtocolEncodeResponse;
import com.bbva.hancock.sdk.models.TransactionConfig;
import org.junit.Test;
import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;
import static org.junit.Assert.*;

public class HancockEthereumClientTest {

    @Test public void testDumb()  {

        String test = "TEST";
        assertEquals(test, "TEST");

    }

    /*@Test public void testConfigInstantiation() throws Exception {

        HancockConfig config = new HancockConfig.Builder()
                .withEnv("custom")
                .withNode("http://mock.node.com", 9999)
                .withAdapter("http://mock.adapter.com", "/base", 9999)
                .build();

        assertEquals(config.getEnv(), "custom");

        HancockConfigNode node = config.getNode();
        assertEquals(node.getHost(), "http://mock.node.com");
        assertEquals(node.getPort(), 9999);

        HancockConfigAdapter adapter = config.getAdapter();
        assertEquals(adapter.getHost(), "http://mock.adapter.com");
        assertEquals(adapter.getBase(), "/base");
        assertEquals(adapter.getPort(), 9999);
        assertEquals(adapter.getResources().get("balance"), "/ethereum/balance/__ADDRESS__");

    }

    @Test public void testGenerateWallet() throws Exception {

        HancockEthereumClient classUnderTest = new HancockEthereumClient();
        EthereumWallet wallet = classUnderTest.generateWallet();
        assertTrue("Wallet should have an address", wallet.getAddress() instanceof String);
        assertTrue("Wallet should have an address", wallet.getPublicKey() instanceof String);
        assertTrue("Wallet should have an address", wallet.getPrivateKey() instanceof String);

        System.out.println("Address =>" + wallet.getAddress());
        System.out.println("PublicKey =>" + wallet.getPublicKey());
        System.out.println("PrivateKey =>" + wallet.getPrivateKey());

    }

    @Test public void testCreateRawTransaction() throws Exception {

        HancockEthereumClient classUnderTest = new HancockEthereumClient();
        EthereumWallet wallet = classUnderTest.generateWallet();

        BigInteger nonce = BigInteger.valueOf(1);
        BigInteger gasPrice = BigInteger.valueOf(111);
        BigInteger gasLimit = BigInteger.valueOf(222);
        BigInteger value = BigInteger.valueOf(333);
        String to = wallet.getAddress();
        String data = "0xwhatever";

        EthereumRawTransaction rawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value, data);

        assertTrue("RawTransaction is well constructed ", rawTransaction instanceof EthereumRawTransaction);
        assertTrue("RawTransaction web3 instance is well constructed ", rawTransaction.getWeb3Instance() instanceof RawTransaction);
        assertTrue("RawTransaction has nonce ", rawTransaction.getNonce() instanceof BigInteger);
        assertTrue("RawTransaction has gasPrice ", rawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has gasLimit ", rawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has to ", rawTransaction.getTo() instanceof String);
        assertTrue("RawTransaction has value ", rawTransaction.getValue() instanceof BigInteger);
        assertTrue("RawTransaction has value ", rawTransaction.getData() instanceof String);


        rawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value);

        assertTrue("RawTransaction has value ", rawTransaction.getValue() instanceof BigInteger);
        assertTrue("RawTransaction has value ", rawTransaction.getData() == "");


        rawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, data);

        assertTrue("RawTransaction has value ", rawTransaction.getValue().equals(BigInteger.ZERO));
        assertTrue("RawTransaction has value ", rawTransaction.getData() instanceof String);

    }

    @Test public void testSignTransaction() throws Exception {

        HancockEthereumClient classUnderTest = new HancockEthereumClient();
        EthereumWallet wallet = classUnderTest.generateWallet();

        BigInteger nonce = BigInteger.valueOf(1);
        BigInteger gasPrice = BigInteger.valueOf(111);
        BigInteger gasLimit = BigInteger.valueOf(222);
        BigInteger value = BigInteger.valueOf(333);
        String to = wallet.getAddress();
        String privateKey = wallet.getPrivateKey();

        EthereumRawTransaction rawTransaction = new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value);
        String signedTransaction = classUnderTest.signTransaction(rawTransaction, privateKey);

        assertTrue("transaction signed successfully", signedTransaction instanceof String);

        System.out.println("Signed tx =>" + signedTransaction);

    }

     @Test public void testAdaptTransfer() throws Exception {

         HancockConfig config = new HancockConfig.Builder()
                 .withAdapter("http:localhost","", 3004)
                 .build();
         HancockEthereumClient classUnderTest = new HancockEthereumClient(config);
         EthereumTransferRequest transferRequest = new EthereumTransferRequest(
                 "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
                 "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d",
                 new BigInteger("0260941720000000000").toString(),
                 "test test"
         );

         EthereumRawTransaction rawtx = classUnderTest.adaptTransfer(transferRequest);

         assertTrue("transaction adapted successfully", rawtx instanceof EthereumRawTransaction);

         System.out.println("rawtx =>" + rawtx);

     }

     @Test public void testTransfer() throws Exception {

         HancockConfig config = new HancockConfig.Builder()
                 .withAdapter("http:localhost","", 3004)
                 .build();
         HancockEthereumClient classUnderTest = new HancockEthereumClient(config);

         TransactionConfig txConfig = new TransactionConfig.Builder()
             .withPrivateKey("0x6c47653f66ac9b733f3b8bf09ed3d300520b4d9c78711ba90162744f5906b1f8")
             .build();

         EthereumTransferRequest txRequest = new EthereumTransferRequest(
            "0x6c0a14f7561898b9ddc0c57652a53b2c6665443e",
            "0xde8e772f0350e992ddef81bf8f51d94a8ea9216d",
            new BigInteger("0260941720000000000").toString(),
            "test test"
         );

         String rawtx = classUnderTest.transfer(txRequest, txConfig);

         assertTrue("transaction adapted successfully", rawtx instanceof String);

         System.out.println("rawtx =>" + rawtx);

     }

    @Test public void testGetBalance() throws Exception {

        HancockConfig config = new HancockConfig.Builder()
                .withAdapter("http://localhost","", 3004)
                .build();
        HancockEthereumClient classUnderTest = new HancockEthereumClient(config);

        BigInteger balance = classUnderTest.getBalance("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        assertTrue("transaction signed successfully", balance.compareTo(BigInteger.valueOf(0)) == 1);

        System.out.println("Balance =>" + balance.toString());

    }

    @Test public void testDecodeProtocol() throws Exception {

        HancockConfig config = new HancockConfig.Builder()
                .withAdapter("http://localhost","", 3004)
                .build();
        HancockEthereumClient classUnderTest = new HancockEthereumClient(config);

        HancockProtocolDecodeResponse response = classUnderTest.decodeProtocol("hancock://qr?code=%7B%22action%22%3A%22transfer%22%2C%22body%22%3A%7B%22value%22%3A%2210%22%2C%22data%22%3A%22dafsda%22%2C%22to%22%3A%220x1234%22%7D%2C%22dlt%22%3A%22ethereum%22%7D");

        assertTrue("transaction signed successfully", response.getTo().equals("0x1234"));

        System.out.println("Action =>" + response.getAction());

    }

    @Test public void testEncodeProtocol() throws Exception {

        HancockConfig config = new HancockConfig.Builder()
                .withAdapter("http://localhost","", 3004)
                .build();
        HancockEthereumClient classUnderTest = new HancockEthereumClient(config);

        HancockProtocolEncodeResponse response = classUnderTest.encodeProtocol(HancockProtocolAction.transfer, new BigInteger("10"), "0x1234", "dafsda", HancockProtocolDlt.ethereum);

        assertTrue("transaction signed successfully", response.getCode().equals("hancock://qr?code=%7B%22action%22%3A%22transfer%22%2C%22body%22%3A%7B%22value%22%3A%2210%22%2C%22data%22%3A%22dafsda%22%2C%22to%22%3A%220x1234%22%7D%2C%22dlt%22%3A%22ethereum%22%7D"));


    }*/
}
