package com.bbva.hancock.sdk.dlt.ethereum.models.transaction;

public class EthereumTransactionResponse {

    private Boolean success;

    private String txHash;

    public EthereumTransactionResponse(Boolean success) {
        this.success = success;
    }

    public EthereumTransactionResponse(Boolean success, String txHash) {
        this.success = success;
        this.txHash = txHash;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getTxHash() {
        return txHash;
    }
}
