package com.bbva.hancock.sdk.dlt.ethereum.models.wallet;

import java.io.Serializable;

class GetBalanceResponseResult implements Serializable {
    private static final long serialVersionUID = 5731921709163217716L;
    public int code;
    public String description;

    public GetBalanceResponseResult() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}

class GetBalanceResponseData implements Serializable {
    private static final long serialVersionUID = 3983747032686948886L;
    public String balance;

    public GetBalanceResponseData() {
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(final String balance) {
        this.balance = balance;
    }
}

public class GetBalanceResponse implements Serializable {
    private static final long serialVersionUID = 4224256941003532491L;
    public GetBalanceResponseResult result;
    public GetBalanceResponseData data;

    public GetBalanceResponse() {
    }

    public String getBalance() {
        return this.data.balance;
    }

    public GetBalanceResponseResult getResult() {
        return result;
    }

    public void setResult(final GetBalanceResponseResult result) {
        this.result = result;
    }

    public GetBalanceResponseData getData() {
        return data;
    }

    public void setData(final GetBalanceResponseData data) {
        this.data = data;
    }
}
