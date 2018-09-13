package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.HancockGenericResponse;

import org.web3j.protocol.core.methods.response.Transaction;

public class EthereumAdaptInvokeResponse {

    private EthereumTransaction data;
    private HancockGenericResponse result;

    public EthereumAdaptInvokeResponse(EthereumTransaction data, HancockGenericResponse result) {
        this.data = data;
        this.result = result;
    }

    public EthereumTransaction getData() {
        return data;
    }

    public void setData(EthereumTransaction data) {
        this.data = data;
    }

    public HancockGenericResponse getResult() {
        return result;
    }

    public void setResult(HancockGenericResponse result) {
        this.result = result;
    }
}