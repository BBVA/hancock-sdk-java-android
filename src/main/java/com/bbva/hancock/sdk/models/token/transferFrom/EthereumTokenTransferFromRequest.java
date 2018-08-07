package com.bbva.hancock.sdk.models.token.transferFrom;

import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.models.token.transfer.EthereumTokenTransferRequest;
import com.bbva.hancock.sdk.models.util.ValidateParameters;

public class EthereumTokenTransferFromRequest extends EthereumTokenTransferRequest {

    private String sender;

    public EthereumTokenTransferFromRequest(String from, String sender, String to, String value, String addressOrAlias) throws HancockException {
        super(from, to, value, addressOrAlias);
        ValidateParameters.checkForContent(sender);
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }
}