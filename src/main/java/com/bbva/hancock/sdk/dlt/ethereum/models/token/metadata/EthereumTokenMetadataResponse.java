package com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata;

import com.bbva.hancock.sdk.models.HancockGenericResponse;

public class EthereumTokenMetadataResponse {
    private HancockGenericResponse result;
    public EthereumTokenMetadata data;

    public EthereumTokenMetadataResponse(HancockGenericResponse result) {
      this.result = result;
    }
    
    public HancockGenericResponse getResult() {
        return result;
    }
    
    public EthereumTokenMetadata getTokenMetadata() {
        return this.data;
    }
}
