package com.bbva.hancock.sdk.dlt.ethereum.models;

import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenInstance;
import com.bbva.hancock.sdk.models.HancockGenericResponse;

import java.io.Serializable;
import java.util.ArrayList;

public class EthereumSmartContractRetrieveResponse implements Serializable {

    private static final long serialVersionUID = -5252822729384368836L;
    private ArrayList<EthereumTokenInstance> data;
    private HancockGenericResponse result;

    public EthereumSmartContractRetrieveResponse() {

    }

    public EthereumSmartContractRetrieveResponse(final ArrayList<EthereumTokenInstance> data, final HancockGenericResponse result) {
        this.data = data;
        this.result = result;
    }

    public ArrayList<EthereumTokenInstance> getData() {
        return data;
    }

    public HancockGenericResponse getResult() {
        return result;
    }

    public void setData(final ArrayList<EthereumTokenInstance> data) {
        this.data = data;
    }

    public void setResult(final HancockGenericResponse result) {
        this.result = result;
    }
}
