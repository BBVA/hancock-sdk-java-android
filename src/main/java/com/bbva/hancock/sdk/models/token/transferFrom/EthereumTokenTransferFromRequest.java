package com.bbva.hancock.sdk.models.token.transferFrom;

import com.bbva.hancock.sdk.models.token.transfer.EthereumTokenTransferRequest;

public class EthereumTokenTransferFromRequest extends EthereumTokenTransferRequest {

    private String sender;

    public EthereumTokenTransferFromRequest(String from, String sender, String to, String value, String addressOrAlias) {
        super(from, to, value, addressOrAlias);
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }
}