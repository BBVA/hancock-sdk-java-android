package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import com.bbva.hancock.sdk.models.HancockGenericResponse;

import java.io.Serializable;

public class EthereumRegisterResponse implements Serializable {

    private static final long serialVersionUID = -6725098586144245607L;
    private HancockGenericResponse result;

    public EthereumRegisterResponse() {

    }

    public EthereumRegisterResponse(final HancockGenericResponse result) {
        this.result = result;
    }

    public HancockGenericResponse getResult() {
        return result;
    }

    public void setResult(final HancockGenericResponse result) {
        this.result = result;
    }

}
