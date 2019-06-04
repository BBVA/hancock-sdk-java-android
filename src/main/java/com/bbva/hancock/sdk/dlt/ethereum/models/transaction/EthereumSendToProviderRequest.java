package com.bbva.hancock.sdk.dlt.ethereum.models.transaction;

import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;

import java.io.Serializable;

public class EthereumSendToProviderRequest implements Serializable {

    private static final long serialVersionUID = -6030145258465765966L;
    private EthereumTransaction rawTx;
    private String provider;
    private String backUrl;

    public EthereumSendToProviderRequest() {
    }

    public EthereumSendToProviderRequest(final EthereumTransaction rawTx, final String provider) {
        this.rawTx = rawTx;
        this.provider = provider;
    }

    public EthereumSendToProviderRequest(final EthereumTransaction rawTx, final String provider, final String backUrl) {
        this.rawTx = rawTx;
        this.provider = provider;
        this.backUrl = backUrl;
    }

    public EthereumTransaction getRawTx() {
        return rawTx;
    }

    public void setRawTx(final EthereumTransaction rawTx) {
        this.rawTx = rawTx;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(final String provider) {
        this.provider = provider;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(final String backUrl) {
        this.backUrl = backUrl;
    }

}
