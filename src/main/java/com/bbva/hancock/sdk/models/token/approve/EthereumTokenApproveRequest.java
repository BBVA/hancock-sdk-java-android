package com.bbva.hancock.sdk.models.token.approve;

import com.bbva.hancock.sdk.models.EthereumTransferRequest;

public class EthereumTokenApproveRequest extends EthereumTransferRequest{

    private String spender;
    private String addressOrAlias;

    public EthereumTokenApproveRequest(String from, String spender, String value, String addressOrAlias) {
        super(from,value);
        this.spender = spender;
        this.addressOrAlias = addressOrAlias;
    }

    public String getSpender() {
        return spender;
    }

    public String getAddressOrAlias() {
        return addressOrAlias;
    }
}