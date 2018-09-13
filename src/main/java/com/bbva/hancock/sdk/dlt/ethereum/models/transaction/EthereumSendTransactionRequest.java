package com.bbva.hancock.sdk.dlt.ethereum.models.transaction;

import com.bbva.hancock.sdk.dlt.ethereum.EthereumRawTransaction;

public class EthereumSendTransactionRequest {

    private EthereumRawTransaction tx;

    public EthereumSendTransactionRequest(EthereumRawTransaction tx) {
        this.tx = tx;
    }
}
