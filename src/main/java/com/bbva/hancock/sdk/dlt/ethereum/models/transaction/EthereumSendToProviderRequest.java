package com.bbva.hancock.sdk.dlt.ethereum.models.transaction;

import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;

import java.io.Serializable;

public class EthereumSendToProviderRequest implements Serializable {

    private EthereumTransaction rawTx;
    private String provider;
    private String backUrl;

    public EthereumSendToProviderRequest() {
    }

    public EthereumSendToProviderRequest(EthereumTransaction rawTx, String provider) {
        this.rawTx = rawTx;
        this.provider = provider;
    }

    public EthereumSendToProviderRequest(EthereumTransaction rawTx, String provider, String backUrl) {
        this.rawTx = rawTx;
        this.provider = provider;
        this.backUrl = backUrl;
    }

    public EthereumTransaction getRawTx() {
        return rawTx;
    }

    public void setRawTx(EthereumTransaction rawTx) {
        this.rawTx = rawTx;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

}
