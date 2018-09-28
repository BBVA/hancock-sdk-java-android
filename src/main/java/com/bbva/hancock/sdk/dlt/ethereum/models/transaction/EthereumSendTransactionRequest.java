package com.bbva.hancock.sdk.dlt.ethereum.models.transaction;

import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;

public class EthereumSendTransactionRequest {

    private EthereumTransaction tx;

    public EthereumSendTransactionRequest(EthereumTransaction tx) {
        this.tx = tx;
    }

    public EthereumTransaction getTx() {
        return tx;
    }
}
