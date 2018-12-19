package com.bbva.hancock.sdk.dlt.ethereum.models.transaction;

import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;

import java.io.Serializable;

public class EthereumSendTransactionRequest implements Serializable {

    private static final long serialVersionUID = -9164963604577934249L;
    private EthereumTransaction tx;

    public EthereumSendTransactionRequest() {

    }

    public EthereumSendTransactionRequest(final EthereumTransaction tx) {
        this.tx = tx;
    }

    public EthereumTransaction getTx() {
        return tx;
    }

    public void setTx(final EthereumTransaction tx) {
        this.tx = tx;
    }
}
