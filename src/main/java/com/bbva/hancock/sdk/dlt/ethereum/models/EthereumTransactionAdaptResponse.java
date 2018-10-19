package com.bbva.hancock.sdk.dlt.ethereum.models;

import com.bbva.hancock.sdk.models.HancockGenericResponse;

public class EthereumTransactionAdaptResponse {

    private EthereumTransaction data;
    private HancockGenericResponse result;

    public EthereumTransactionAdaptResponse(EthereumTransaction data, HancockGenericResponse result) {
        this.data = data;
        this.result = result;
    }

    public EthereumTransaction getData() {
        return data;
    }

    public void setData(EthereumTransaction data) {
        this.data = data;
    }

    public HancockGenericResponse getResult() {
        return result;
    }

    public void setResult(HancockGenericResponse result) {
        this.result = result;
    }
}
