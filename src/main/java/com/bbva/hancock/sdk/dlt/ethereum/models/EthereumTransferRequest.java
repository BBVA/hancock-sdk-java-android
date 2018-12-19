package com.bbva.hancock.sdk.dlt.ethereum.models;

import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.util.ValidateParameters;

import java.io.Serializable;

public class EthereumTransferRequest implements Serializable {

    private static final long serialVersionUID = 6336492127679552080L;
    private String from;
    private String to;
    private String value;
    private String data;

    public EthereumTransferRequest() {
    }

    public EthereumTransferRequest(final String from, final String to, final String value, final String data) throws HancockException {
        ValidateParameters.checkForContent(from, "from");
        this.from = from;
        ValidateParameters.checkForContent(to, "to");
        this.to = to;
        ValidateParameters.checkForContent(value, "value");
        this.value = value;
        this.data = data;

    }

    public EthereumTransferRequest(final String from, final String to, final String value) throws HancockException {
        ValidateParameters.checkForContent(from, "from");
        this.from = from;
        ValidateParameters.checkForContent(to, "to");
        this.to = to;
        ValidateParameters.checkForContent(value, "value");
        this.value = value;
    }

    public EthereumTransferRequest(final String from, final String value) throws HancockException {
        ValidateParameters.checkForContent(from, "from");
        this.from = from;
        ValidateParameters.checkForContent(value, "value");
        this.value = value;
    }

    public EthereumTransferRequest(final String from) throws HancockException {
        ValidateParameters.checkForContent(from, "from");
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getValue() {
        return value;
    }

    public String getData() {
        return data;
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    public void setTo(final String to) {
        this.to = to;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public void setData(final String data) {
        this.data = data;
    }
}
