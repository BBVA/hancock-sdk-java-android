package com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata;

import com.bbva.hancock.sdk.models.HancockGenericResponse;

import java.io.Serializable;

public class EthereumTokenMetadataResponse implements Serializable {

    private static final long serialVersionUID = -3782067033479737624L;
    private HancockGenericResponse result;
    public EthereumTokenMetadata data;

    public EthereumTokenMetadataResponse() {

    }

    public EthereumTokenMetadataResponse(final HancockGenericResponse result) {
        this.result = result;
    }

    public HancockGenericResponse getResult() {
        return result;
    }

    public EthereumTokenMetadata getTokenMetadata() {
        return data;
    }

    public EthereumTokenMetadata getData() {
        return data;
    }

    public void setData(final EthereumTokenMetadata data) {
        this.data = data;
    }
}
