package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import com.bbva.hancock.sdk.models.HancockGenericResponse;

import java.io.Serializable;

public class EthereumCallResponse implements Serializable {

    private static final long serialVersionUID = -1879043266148806288L;
    
    private Object data;
    private HancockGenericResponse result;

    public EthereumCallResponse() {

    }

    public EthereumCallResponse(final Object data, final HancockGenericResponse result) {
        this.data = data;
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(final Object data) {
        this.data = data;
    }

    public HancockGenericResponse getResult() {
        return result;
    }

    public void setResult(final HancockGenericResponse result) {
        this.result = result;
    }

}
