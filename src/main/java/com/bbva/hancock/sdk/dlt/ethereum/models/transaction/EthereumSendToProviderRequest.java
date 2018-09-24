package com.bbva.hancock.sdk.dlt.ethereum.models.transaction;

import com.bbva.hancock.sdk.dlt.ethereum.EthereumRawTransaction;

public class EthereumSendToProviderRequest {

    private EthereumRawTransaction rawTx;
    private String provider;
    private String backUrl;

    public EthereumSendToProviderRequest(EthereumRawTransaction rawTx, String provider) {
        this.rawTx = rawTx;
        this.provider = provider;
    }

    public EthereumSendToProviderRequest(EthereumRawTransaction rawTx, String provider, String backUrl) {
        this.rawTx = rawTx;
        this.provider = provider;
        this.backUrl = backUrl;
    }

    public EthereumRawTransaction getRawTx() {
        return rawTx;
    }

    public void setRawTx(EthereumRawTransaction rawTx) {
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
