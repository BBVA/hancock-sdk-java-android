package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import java.io.Serializable;

public class EthereumDeploySendResponse implements Serializable {

    private static final long serialVersionUID = -7775062781676108489L;
    private Boolean success;

    private String transactionHash;

    public EthereumDeploySendResponse() {

    }

    public EthereumDeploySendResponse(final Boolean success) {
        this.success = success;
    }

    public EthereumDeploySendResponse(final Boolean success, final String transactionHash) {
        this.success = success;
        this.transactionHash = transactionHash;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setSuccess(final Boolean success) {
        this.success = success;
    }

    public void setTransactionHash(final String transactionHash) {
        this.transactionHash = transactionHash;
    }
}
