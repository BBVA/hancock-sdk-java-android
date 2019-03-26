package com.bbva.hancock.sdk.dlt.ethereum.models;

import com.bbva.hancock.sdk.models.HancockGenericResponse;

import java.io.Serializable;

public class EthereumTransactionAdaptResponse implements Serializable {

    private static final long serialVersionUID = 363843238553138048L;

    private EthereumTransaction data;
    private HancockGenericResponse result;

    public EthereumTransactionAdaptResponse() {
    }

    public EthereumTransactionAdaptResponse(final EthereumTransaction data, final HancockGenericResponse result) {
        this.data = data;
        this.result = result;
    }

    public EthereumTransaction getData() {
        return data;
    }

    public void setData(final EthereumTransaction data) {
        this.data = data;
    }

    public HancockGenericResponse getResult() {
        return result;
    }

    public void setResult(final HancockGenericResponse result) {
        this.result = result;
    }
}
