package com.bbva.hancock.sdk.dlt.ethereum.models.transaction;

import java.io.Serializable;

public class EthereumTransactionResponse implements Serializable {

    private static final long serialVersionUID = -7775062781676108489L;
    private Boolean success;

    private String txHash;

    public EthereumTransactionResponse() {

    }

    public EthereumTransactionResponse(final Boolean success) {
        this.success = success;
    }

    public EthereumTransactionResponse(final Boolean success, final String txHash) {
        this.success = success;
        this.txHash = txHash;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setSuccess(final Boolean success) {
        this.success = success;
    }

    public void setTxHash(final String txHash) {
        this.txHash = txHash;
    }

}
