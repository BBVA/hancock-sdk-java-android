package com.bbva.hancock.sdk.dlt.ethereum.models;

import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenInstance;
import com.bbva.hancock.sdk.models.HancockGenericResponse;

import java.util.ArrayList;

public class EthereumSmartContractRetrieveResponse {

    private ArrayList<EthereumTokenInstance> data;
    private HancockGenericResponse result;

    public EthereumSmartContractRetrieveResponse(ArrayList<EthereumTokenInstance> data, HancockGenericResponse result) {
        this.data = data;
        this.result = result;
    }

    public ArrayList<EthereumTokenInstance> getData() {
        return data;
    }

    public HancockGenericResponse getResult() {
        return result;
    }
}
