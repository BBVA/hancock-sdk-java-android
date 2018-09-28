package com.bbva.hancock.sdk.dlt.ethereum.models;

public class EthereumBody {

    private String from;
    private String to;
    private String value;

    public EthereumBody(String from) {
        this.from = from;
    }

    public EthereumBody(String from, String value) {
        this.from = from;
        this.value = value;
    }

    public EthereumBody(String from, String to, String value) {
        this.from = from;
        this.to = to;
        this.value = value;
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
}
