package com.bbva.hancock.sdk.dlt.ethereum.models.token;

import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.util.ValidateParameters;

import java.io.Serializable;

public class EthereumTokenRequest implements Serializable {

    private static final long serialVersionUID = 2500137443965327085L;
    private EthereumTokenBody body;
    private String addressOrAlias;
    private String encodeUrl;

    public EthereumTokenRequest() {
    }

    public EthereumTokenRequest(final EthereumTokenBody body, final String addressOrAlias, final String encodeUrl) throws HancockException {
        this.body = body;
        ValidateParameters.checkForContent(addressOrAlias, "Address or Alias");
        this.addressOrAlias = addressOrAlias;
        this.encodeUrl = encodeUrl;
    }

    public EthereumTokenRequest(final String addressOrAlias, final String encodeUrl) throws HancockException {
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

    public void setBody(final EthereumTokenBody body) {
        this.body = body;
    }

    public void setAddressOrAlias(final String addressOrAlias) {
        this.addressOrAlias = addressOrAlias;
    }

    public void setEncodeUrl(final String encodeUrl) {
        this.encodeUrl = encodeUrl;
    }

}
