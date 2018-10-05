package com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata;

class EthereumTokenMetadataResponseResult {
    public int code;
    public String description;
}

public class EthereumTokenMetadataResponse {
    public EthereumTokenMetadataResponseResult result;
    public EthereumTokenMetadata data;

    public EthereumTokenMetadata getTokenMetadata() {
        return this.data;
    }
}
