package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import com.bbva.hancock.sdk.models.HancockGenericResponse;

public class EthereumRegisterResponse {

    private HancockGenericResponse result;

    public EthereumRegisterResponse(HancockGenericResponse result) {
        this.result = result;
    }

    public HancockGenericResponse getResult() {
        return result;
    }
}
