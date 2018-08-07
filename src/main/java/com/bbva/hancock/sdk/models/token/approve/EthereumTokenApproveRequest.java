package com.bbva.hancock.sdk.models.token.approve;

import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.models.util.ValidateParameters;

public class EthereumTokenApproveRequest extends EthereumTransferRequest{

    private String spender;
    private String addressOrAlias;

    public EthereumTokenApproveRequest(String from, String spender, String value, String addressOrAlias) throws HancockException {
        super(from,value);
        ValidateParameters.checkForContent(spender);
        this.spender = spender;
        ValidateParameters.checkForContent(addressOrAlias);
        this.addressOrAlias = addressOrAlias;
    }

    public String getSpender() {
        return spender;
    }

    public String getAddressOrAlias() {
        return addressOrAlias;
    }
}