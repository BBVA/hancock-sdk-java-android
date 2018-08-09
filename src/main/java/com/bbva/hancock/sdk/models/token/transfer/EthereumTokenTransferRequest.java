package com.bbva.hancock.sdk.models.token.transfer;

import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.models.util.ValidateParameters;

public class EthereumTokenTransferRequest extends EthereumTransferRequest {

    private String addressOrAlias;

    public EthereumTokenTransferRequest(String from, String to, String value, String addressOrAlias) throws HancockException {
        super(from, to, value);
        ValidateParameters.checkForContent(addressOrAlias, "address or alias");
        this.addressOrAlias = addressOrAlias;
    }

    public String getAddressOrAlias() {
        return addressOrAlias;
    }
}