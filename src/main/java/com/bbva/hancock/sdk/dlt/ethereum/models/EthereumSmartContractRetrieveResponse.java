package com.bbva.hancock.sdk.dlt.ethereum.models;

import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumContractInstance;
import com.bbva.hancock.sdk.models.HancockGenericResponse;

import java.util.ArrayList;

public class EthereumSmartContractRetrieveResponse {

    private ArrayList<EthereumContractInstance> data;
    private HancockGenericResponse result;

    public EthereumSmartContractRetrieveResponse(ArrayList<EthereumContractInstance> data, HancockGenericResponse result) {
        this.data = data;
        this.result = result;
    }

    public ArrayList<EthereumContractInstance> getData() {
        return data;
    }

    public HancockGenericResponse getResult() {
        return result;
    }
}
