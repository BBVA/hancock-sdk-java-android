package com.bbva.hancock.sdk;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import org.junit.Test;
import org.web3j.crypto.RawTransaction;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import static org.junit.Assert.*;

public class HancockEthereumClientTest {

    @Test public void testClientInstantiation() {

        HancockEthereumClient classUnderTest = new HancockEthereumClient();
        assertNotNull("HancockEthereumClient should be not null", classUnderTest);
        assertTrue("HancockEthereumClient should instantiate correctly", classUnderTest instanceof HancockEthereumClient);

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

        EthereumRawTransaction rawTransaction = classUnderTest.createRawTransaction(nonce, gasPrice, gasLimit, to, value, data);

        assertTrue("RawTransaction is well constructed ", rawTransaction instanceof EthereumRawTransaction);
        assertTrue("RawTransaction web3 instance is well constructed ", rawTransaction.getWeb3Instance() instanceof RawTransaction);
        assertTrue("RawTransaction has nonce ", rawTransaction.getNonce() instanceof BigInteger);
        assertTrue("RawTransaction has gasPrice ", rawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has gasLimit ", rawTransaction.getGasPrice() instanceof BigInteger);
        assertTrue("RawTransaction has to ", rawTransaction.getTo() instanceof String);
        assertTrue("RawTransaction has value ", rawTransaction.getValue() instanceof BigInteger);
        assertTrue("RawTransaction has value ", rawTransaction.getData() instanceof String);


        rawTransaction = classUnderTest.createRawTransaction(nonce, gasPrice, gasLimit, to, value);

        assertTrue("RawTransaction has value ", rawTransaction.getValue() instanceof BigInteger);
        assertTrue("RawTransaction has value ", rawTransaction.getData() == "");


        rawTransaction = classUnderTest.createRawTransaction(nonce, gasPrice, gasLimit, to, data);

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

        EthereumRawTransaction rawTransaction = classUnderTest.createRawTransaction(nonce, gasPrice, gasLimit, to, value);
        String signedTransaction = classUnderTest.signTransaction(rawTransaction, privateKey);

        assertTrue("transaction signed successfully", signedTransaction instanceof String);

        System.out.println("Signed tx =>" + signedTransaction);

    }

}
