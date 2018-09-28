package com.bbva.hancock.sdk.dlt.ethereum.models.wallet;

class GetBalanceResponseResult {
    public int code;
    public String description;
}

class GetBalanceResponseData {
    public String balance;
}

public class GetBalanceResponse {
    public GetBalanceResponseResult result;
    public GetBalanceResponseData data;

    public String getBalance() {
        return this.data.balance;
    }
}
