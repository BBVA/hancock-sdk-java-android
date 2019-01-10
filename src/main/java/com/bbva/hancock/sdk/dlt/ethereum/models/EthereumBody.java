package com.bbva.hancock.sdk.dlt.ethereum.models;

import java.io.Serializable;

public class EthereumBody implements Serializable {

    private static final long serialVersionUID = -4442436200964932879L;
    private String from;
    private String to;
    private String value;

    public EthereumBody() {

    }

    public EthereumBody(final String from) {
        this.from = from;
    }

    public EthereumBody(final String from, final String value) {
        this.from = from;
        this.value = value;
    }

    public EthereumBody(final String from, final String to, final String value) {
        this.from = from;
        this.to = to;
        this.value = value;
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

}
