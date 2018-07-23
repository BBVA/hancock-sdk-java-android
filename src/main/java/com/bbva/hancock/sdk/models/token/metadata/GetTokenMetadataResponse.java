package com.bbva.hancock.sdk.models.token.metadata;

class GetTokenMetadataResponseResult {
    public int code;
    public String description;
}

public class GetTokenMetadataResponse {
    public GetTokenMetadataResponseResult result;
    public GetTokenMetadataResponseData data;

    public GetTokenMetadataResponseData getTokenMetadata() {
        return this.data;
    }
}
