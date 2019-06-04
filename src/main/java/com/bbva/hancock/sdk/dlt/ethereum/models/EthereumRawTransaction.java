package com.bbva.hancock.sdk.dlt.ethereum.models;

import org.web3j.crypto.RawTransaction;

import java.io.Serializable;
import java.math.BigInteger;

public final class EthereumRawTransaction implements Serializable {

    private static final long serialVersionUID = -6501902289201200387L;

    private String from;
    private String to;
    private BigInteger nonce;
    private BigInteger value;
    private String data;
    private BigInteger gasPrice;
    private BigInteger gasLimit;

    public EthereumRawTransaction() {

    }

    public EthereumRawTransaction(final String from, final String to, final BigInteger nonce, final BigInteger value, final String data, final BigInteger gasPrice, final BigInteger gas) {
        this.from = from;
        this.to = to;
        this.nonce = nonce;
        this.value = value;
        this.data = data;
        this.gasPrice = gasPrice;
        gasLimit = gas;
    }

    public EthereumRawTransaction(final String to, final BigInteger nonce, final BigInteger value, final String data, final BigInteger gasPrice, final BigInteger gasLimit) {
        this.to = to;
        this.nonce = nonce;
        this.value = value;
        this.data = data;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
    }

    public EthereumRawTransaction(final String to, final BigInteger nonce, final BigInteger value, final BigInteger gasPrice, final BigInteger gas) {
        this.to = to;
        this.nonce = nonce;
        this.value = value;
        this.gasPrice = gasPrice;
        gasLimit = gas;
    }

    public EthereumRawTransaction(final EthereumTransaction tx) {
        from = tx.getFrom();
        to = tx.getTo();
        nonce = tx.getNonce() != null ? new BigInteger(tx.getNonce().substring(2), 16) : BigInteger.ZERO;
        value = tx.getValue() != null ? new BigInteger(tx.getValue().substring(2), 16) : BigInteger.ZERO;
        data = tx.getData();
        gasPrice = tx.getGasPrice() != null ? new BigInteger(tx.getGasPrice().substring(2), 16) : BigInteger.ZERO;
        gasLimit = tx.getGas() != null ? new BigInteger(tx.getGas().substring(2), 16) : BigInteger.ZERO;
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

    public void setFrom(final String from) {
        this.from = from;
    }

    public void setTo(final String to) {
        this.to = to;
    }

    public void setNonce(final BigInteger nonce) {
        this.nonce = nonce;
    }

    public void setValue(final BigInteger value) {
        this.value = value;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public void setGasPrice(final BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

    public void setGasLimit(final BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public RawTransaction getWeb3Instance() {
        return RawTransaction.createTransaction(
                getNonce(),
                getGasPrice(),
                getGasLimit(),
                getTo(),
                getValue(),
                getData()
        );
    }

}
