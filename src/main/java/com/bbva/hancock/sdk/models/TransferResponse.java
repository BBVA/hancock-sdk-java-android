package com.bbva.hancock.sdk.models;

import java.math.BigInteger;

class TransferResponseResult {
    public int code;
    public String description;
}

class TransferResponseData {
    public String from;
    public String to;
    public String value;
    public String data;
    public String gas;
    public String gasPrice;
    public String nonce;
}

public class TransferResponse {
    public TransferResponseResult result;
    public TransferResponseData data;

    public BigInteger getNonce(){
        return new BigInteger(data.nonce.substring(2), 16);
    }
    public BigInteger getGasPrice(){
        return new BigInteger(data.gasPrice.substring(2), 16);
    }
    public BigInteger getGas(){
        return new BigInteger(data.gas.substring(2), 16);
    }
    public BigInteger getValue(){
        return new BigInteger(data.value.substring(2), 16);
    }
    public String getData(){
        return data.data;
    }
    public String getFrom(){
        return data.from;
    }
    public String getTo(){
        return data.to;
    }
}
