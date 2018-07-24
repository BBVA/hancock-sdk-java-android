package com.bbva.hancock.sdk.models.token.transfer;

import com.bbva.hancock.sdk.models.EthereumTransferRequest;

public class EthereumTokenTransferRequest extends EthereumTransferRequest {

    private String addressOrAlias;

    public EthereumTokenTransferRequest(String from, String to, String value, String addressOrAlias) {
        super(from, to, value);
        this.addressOrAlias = addressOrAlias;
    }

    public String getAddressOrAlias() {
        return addressOrAlias;
    }
}