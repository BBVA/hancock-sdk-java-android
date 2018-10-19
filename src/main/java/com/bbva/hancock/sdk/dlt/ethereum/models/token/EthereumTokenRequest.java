package com.bbva.hancock.sdk.dlt.ethereum.models.token;

import com.bbva.hancock.sdk.util.ValidateParameters;
import com.bbva.hancock.sdk.exception.HancockException;

public class EthereumTokenRequest {

    private EthereumTokenBody body;
    private String addressOrAlias;
    private String encodeUrl;

    public EthereumTokenRequest(EthereumTokenBody body, String addressOrAlias, String encodeUrl) throws HancockException {
        this.body = body;
        ValidateParameters.checkForContent(addressOrAlias, "Address or Alias");
        this.addressOrAlias = addressOrAlias;
        this.encodeUrl = encodeUrl;
    }

    public EthereumTokenRequest(String addressOrAlias, String encodeUrl) throws HancockException {
        this.addressOrAlias = addressOrAlias;
        ValidateParameters.checkForContent(addressOrAlias, "Address or Alias");
        this.encodeUrl = encodeUrl;
    }

    public EthereumTokenBody getBody() {
        return body;
    }

    public String getAddressOrAlias() {
        return addressOrAlias;
    }

    public String getEncodeUrl() {
        return encodeUrl;
    }

    public void setBody(EthereumTokenBody body) {
        this.body = body;
    }

    public void setAddressOrAlias(String addressOrAlias) {
        this.addressOrAlias = addressOrAlias;
    }

    public void setEncodeUrl(String encodeUrl) {
        this.encodeUrl = encodeUrl;
    }
}
