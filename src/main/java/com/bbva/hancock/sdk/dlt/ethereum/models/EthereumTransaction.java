package com.bbva.hancock.sdk.dlt.ethereum.models;

import java.io.Serializable;

public class EthereumTransaction implements Serializable {

    private static final long serialVersionUID = 7659625467339800148L;

    private String from;
    private String to;
    private String value;
    private String data;
    private String nonce;
    private String gas;
    private String gasPrice;

    public EthereumTransaction() {
    }

    public EthereumTransaction(final String from, final String to, final String value, final String data, final String nonce, final String gas, final String gasPrice) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.data = data;
        this.nonce = nonce;
        this.gas = gas;
        this.gasPrice = gasPrice;
    }

    public EthereumTransaction(final String from, final String to, final String value, final String nonce, final String gas, final String gasPrice) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.nonce = nonce;
        data = "";
        this.gas = gas;
        this.gasPrice = gasPrice;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(final String to) {
        this.to = to;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(final String nonce) {
        this.nonce = nonce;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(final String gas) {
        this.gas = gas;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(final String gasPrice) {
        this.gasPrice = gasPrice;
    }

}
