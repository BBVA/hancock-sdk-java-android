package com.bbva.hancock.sdk.dlt.ethereum.models.token.balance;

import com.bbva.hancock.sdk.models.HancockGenericResponse;

import java.io.Serializable;

public class EthereumTokenBalanceResponse implements Serializable {

    private static final long serialVersionUID = 8602610907944650010L;
    private HancockGenericResponse result;
    private EthereumTokenBalance data;

    public EthereumTokenBalanceResponse() {

    }

    public EthereumTokenBalanceResponse(final HancockGenericResponse result) {
        this.result = result;
    }

    public HancockGenericResponse getResult() {
        return result;
    }

    public EthereumTokenBalance getTokenBalance() {
        return data;
    }

    public EthereumTokenBalance getData() {
        return data;
    }

    public void setData(final EthereumTokenBalance data) {
        this.data = data;
    }
}
