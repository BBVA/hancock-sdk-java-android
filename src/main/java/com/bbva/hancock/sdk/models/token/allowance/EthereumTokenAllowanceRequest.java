package com.bbva.hancock.sdk.models.token.allowance;

import com.bbva.hancock.sdk.models.EthereumTransferRequest;

public class EthereumTokenAllowanceRequest extends EthereumTransferRequest{

    private String tokenOwner;
    private String spender;
    private String addressOrAlias;

    public EthereumTokenAllowanceRequest(String from, String tokenOwner, String spender, String addressOrAlias) {
        super(from);
        this.tokenOwner = tokenOwner;
        this.spender = spender;
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