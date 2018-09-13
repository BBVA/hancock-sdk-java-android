package com.bbva.hancock.sdk.dlt.ethereum.models.transaction;

import com.bbva.hancock.sdk.dlt.ethereum.EthereumRawTransaction;

public class EthereumSendToProviderRequest {

    private EthereumRawTransaction rawTx;
    private String provider;

    public EthereumSendToProviderRequest(EthereumRawTransaction rawTx, String provider) {
        this.rawTx = rawTx;
        this.provider = provider;
    }

}
