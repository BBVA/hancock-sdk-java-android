package com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata;

class GetEthereumTokenMetadataResponseResult {
    public int code;
    public String description;
}

public class GetEthereumTokenMetadataResponse {
    public GetEthereumTokenMetadataResponseResult result;
    public GetEthereumTokenMetadataResponseData data;

    public GetEthereumTokenMetadataResponseData getTokenMetadata() {
        return this.data;
    }
}
