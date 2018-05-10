package com.bbva.hancock.sdk;

import com.bbva.hancock.sdk.config.HancockConfig;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;


/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class HancockEthereumClient {

    private HancockConfig config;

    public HancockEthereumClient() {

        // this.config = this.loadPrivateConfig();
        this.config = HancockConfig.createDefaultConfig();

    }

    public HancockEthereumClient(HancockConfig config) throws Exception {

        // TODO: Support yaml load config on android
        this.config = config;

    }

    public EthereumWallet generateWallet() throws Exception {

        String dir = WalletUtils.getDefaultKeyDirectory();
        return this.generateWallet(dir);

    }

    public EthereumWallet generateWallet(String dir) throws Exception {

        try {

            String pass = String.format("__%s__", Math.floor(Math.random() * 100000000));

            File tmpDirectory = new File(dir);
            tmpDirectory.mkdirs();

            String fileName = WalletUtils.generateNewWalletFile(pass, tmpDirectory, false);
            Credentials credentials = WalletUtils.loadCredentials(pass, tmpDirectory + "/" + fileName);

            tmpDirectory.delete();

            String address = credentials.getAddress();
            String privateKey = "0x" + credentials.getEcKeyPair().getPrivateKey().toString(16);
            String publicKey = "0x" + credentials.getEcKeyPair().getPublicKey().toString(16);

            return new EthereumWallet(address, privateKey, publicKey);

        } catch (Exception error) {

            System.out.println("error: " + error.toString());
            throw new Exception("Error generating wallet");

        }

    }

    public EthereumRawTransaction createRawTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, String data) {
        return new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, data);
    }

    public EthereumRawTransaction createRawTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value) {
        return new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value);
    }

    public EthereumRawTransaction createRawTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data) {
        return new EthereumRawTransaction(nonce, gasPrice, gasLimit, to, value, data);
    }

    public String signTransaction(EthereumRawTransaction rawTransaction, String privateKey) {

        Credentials credentials = Credentials.create(privateKey);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction.getWeb3Instance(), credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        return hexValue;
    }

    public String sendSignedTransaction(String signedTransaction, boolean locally) throws Exception {

        String url = locally
                ? (this.config.getNode().getHost() + ':' + this.config.getNode().getPort())
                : ("");

        return sendSignedTransaction(signedTransaction, locally, url);

    }

    public String sendSignedTransaction(String signedTransaction, boolean locally, String url) throws Exception {

        if (locally) {
            return this.sendSignedTransactionLocally(signedTransaction, url);
        } else {
            return this.sendSignedTransactionRemotely(signedTransaction, url);
        }

    }

    private String sendSignedTransactionLocally(String signedTransaction) throws InterruptedException, ExecutionException {
        String nodeUrl = this.config.getNode().getHost() + ':' + this.config.getNode().getPort();
        return this.sendSignedTransactionLocally(signedTransaction, nodeUrl);
    }

    private String sendSignedTransactionLocally(String signedTransaction, String nodeUrl) throws InterruptedException, ExecutionException {

        // defaults to http://localhost:8545/
        Web3j web3j = Web3jFactory.build(new HttpService(nodeUrl));

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedTransaction).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();
        // poll for transaction response via org.web3j.protocol.Web3j.ethGetTransactionReceipt(<txHash>)

        return transactionHash;

    }

    private String sendSignedTransactionRemotely(String signedTransaction, String backUrl) throws Exception {

       throw new Exception("Not implemented");

    }

    // TODO: Support yaml load config on android
    /*private HancockConfig loadPrivateConfig() {
        Config baseConfig = ConfigFactory.load();
        Config config, specificConfig;

        try {

            specificConfig = ConfigFactory.load(System.getenv("BUILD_ENV"));
            config = baseConfig.withFallback(specificConfig);

        } catch (Exception e) {

            config = baseConfig;

        }

        HancockConfig hancockConfig = ConfigBeanFactory.create(config, HancockConfig.class);
        return hancockConfig;
    }*/


}