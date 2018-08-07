package com.bbva.hancock.sdk.models.token.allowance;

import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.models.util.ValidateParameters;

public class EthereumTokenAllowanceRequest extends EthereumTransferRequest{

    private String tokenOwner;
    private String spender;
    private String addressOrAlias;

    public EthereumTokenAllowanceRequest(String from, String tokenOwner, String spender, String addressOrAlias) throws HancockException {
        super(from);
        ValidateParameters.checkForContent(tokenOwner);
        this.tokenOwner = tokenOwner;
        ValidateParameters.checkForContent(spender);
        this.spender = spender;
        ValidateParameters.checkForContent(addressOrAlias);
        this.addressOrAlias = addressOrAlias;
    }

    public String getTokenOwner() {
        return tokenOwner;
    }

    public String getSpender() {
        return spender;
    }

    public String getAddressOrAlias() {
        return addressOrAlias;
    }
}