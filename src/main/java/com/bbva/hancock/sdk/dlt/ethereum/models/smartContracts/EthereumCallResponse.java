package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import com.bbva.hancock.sdk.models.HancockGenericResponse;

public class EthereumCallResponse {

    private Object data;
    private HancockGenericResponse result;

    public EthereumCallResponse(Object data, HancockGenericResponse result) {
        this.data = data;
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public HancockGenericResponse getResult() {
        return result;
    }

    public void setResult(HancockGenericResponse result) {
        this.result = result;
    }
}
