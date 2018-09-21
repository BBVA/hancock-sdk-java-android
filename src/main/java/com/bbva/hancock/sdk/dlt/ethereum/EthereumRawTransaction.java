package com.bbva.hancock.sdk.dlt.ethereum;

import org.web3j.crypto.RawTransaction;
import java.math.BigInteger;

public final class EthereumRawTransaction {

    private String from;
    private String to;
    private BigInteger nonce;
    private BigInteger value;
    private String data;
    private BigInteger gasPrice;
    private BigInteger gasLimit;

    public EthereumRawTransaction(String from, String to, BigInteger nonce, BigInteger value, String data, BigInteger gasPrice, BigInteger gas) {
        this.from = from;
        this.to = to;
        this.nonce = nonce;
        this.value = value;
        this.data = data;
        this.gasPrice = gasPrice;
        this.gasLimit = gas;
    }

    public EthereumRawTransaction(String to, BigInteger nonce, BigInteger value, String data, BigInteger gasPrice, BigInteger gasLimit) {
        this.to = to;
        this.nonce = nonce;
        this.value = value;
        this.data = data;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
    }

    public EthereumRawTransaction(String to, BigInteger nonce, BigInteger value, BigInteger gasPrice, BigInteger gas) {
        this.to = to;
        this.nonce = nonce;
        this.value = value;
        this.gasPrice = gasPrice;
        this.gasLimit = gas;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public BigInteger getValue() {
        return value;
    }

    public String getData() {
        return data;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public RawTransaction getWeb3Instance() {
        return RawTransaction.createTransaction(
                this.getNonce(),
                this.getGasPrice(),
                this.getGasLimit(),
                this.getTo(),
                this.getValue(),
                this.getData()
        );
    }

}