package com.bbva.hancock.sdk.dlt.ethereum.models.transaction;

import java.io.Serializable;

public class EthereumSignedTransactionRequest implements Serializable {

    private static final long serialVersionUID = 1109157741343089892L;
    private String tx;

    public EthereumSignedTransactionRequest() {
    }

    public EthereumSignedTransactionRequest(final String tx) {
        this.tx = tx;
    }

    public String getTx() {
        return tx;
    }

}
