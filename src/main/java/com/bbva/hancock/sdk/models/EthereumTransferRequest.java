package com.bbva.hancock.sdk.models;

import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.models.util.ValidateParameters;

public class EthereumTransferRequest {

    private String from;
    private String to;
    private String value;
    private String data;

    public EthereumTransferRequest(String from, String to, String value, String data) throws HancockException {
        ValidateParameters.checkForContent(from);
        this.from = from;
        ValidateParameters.checkForContent(to);
        this.to = to;
        ValidateParameters.checkForContent(value);
        this.value = value;
        this.data = data;
        
    }

    public EthereumTransferRequest(String from, String to, String value) throws HancockException {
        ValidateParameters.checkForContent(from);
        this.from = from;
        ValidateParameters.checkForContent(to);
        this.to = to;
        ValidateParameters.checkForContent(value);
        this.value = value;
    }

    public EthereumTransferRequest(String from, String value) throws HancockException {
        ValidateParameters.checkForContent(from);
        this.from = from;
        ValidateParameters.checkForContent(value);
        this.value = value;
    }
    
    public EthereumTransferRequest(String from) throws HancockException {
        ValidateParameters.checkForContent(from);
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