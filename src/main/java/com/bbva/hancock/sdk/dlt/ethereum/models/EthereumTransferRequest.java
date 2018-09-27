package com.bbva.hancock.sdk.dlt.ethereum.models;

import com.bbva.hancock.sdk.util.ValidateParameters;
import com.bbva.hancock.sdk.exception.HancockException;

public class EthereumTransferRequest {

    private String from;
    private String to;
    private String value;
    private String data;

    public EthereumTransferRequest(String from, String to, String value, String data) throws HancockException {
        ValidateParameters.checkForContent(from, "from");
        this.from = from;
        ValidateParameters.checkForContent(to, "to");
        this.to = to;
        ValidateParameters.checkForContent(value, "value");
        this.value = value;
        this.data = data;
        
    }

    public EthereumTransferRequest(String from, String to, String value) throws HancockException {
        ValidateParameters.checkForContent(from, "from");
        this.from = from;
        ValidateParameters.checkForContent(to, "to");
        this.to = to;
        ValidateParameters.checkForContent(value, "value");
        this.value = value;
    }

    public EthereumTransferRequest(String from, String value) throws HancockException {
        ValidateParameters.checkForContent(from, "from");
        this.from = from;
        ValidateParameters.checkForContent(value, "value");
        this.value = value;
    }
    
    public EthereumTransferRequest(String from) throws HancockException {
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
}