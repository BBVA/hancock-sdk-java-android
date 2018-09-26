package com.bbva.hancock.sdk.dlt.ethereum.models;

public class EthereumTransaction {

    private String from;
    private String to;
    private String value;
    private String data = "data";
    private String nonce;
    private String gas;
    private String gasPrice;

    public EthereumTransaction(String from, String to, String value, String data, String nonce, String gas, String gasPrice) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.data = data;
        this.nonce = nonce;
        this.gas = gas;
        this.gasPrice = gasPrice;
    }

    public EthereumTransaction(String from, String to, String value, String nonce, String gas, String gasPrice) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.nonce = nonce;
        this.gas = gas;
        this.gasPrice = gasPrice;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }
}