package com.bbva.hancock.sdk.dlt.ethereum.models.token.register;

import com.bbva.hancock.sdk.models.HancockGenericResponse;

import java.io.Serializable;

public class EthereumTokenRegisterResponse implements Serializable {
    private static final long serialVersionUID = 4981013697079975273L;
    private HancockGenericResponse result;

    public EthereumTokenRegisterResponse() {

    }

    public EthereumTokenRegisterResponse(final HancockGenericResponse result) {
        this.result = result;
    }

    public HancockGenericResponse getResult() {
        return result;
    }

    public void setResult(final HancockGenericResponse result) {
        this.result = result;
    }
}
