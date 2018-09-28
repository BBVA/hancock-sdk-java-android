package com.bbva.hancock.sdk.dlt.ethereum.models.token.allowance;

import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenBody;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenRequest;
import com.bbva.hancock.sdk.util.ValidateParameters;
import com.bbva.hancock.sdk.exception.HancockException;

public class EthereumTokenAllowanceRequest extends EthereumTokenRequest {

    public EthereumTokenAllowanceRequest(String from, String tokenOwner, String spender, String addressOrAlias) throws HancockException {
        super(addressOrAlias, "tokenAllowance");
        EthereumTokenBody body = new EthereumTokenBody(from);
        ValidateParameters.checkForContent(tokenOwner, "tokenOwner");
        body.setTokenOwner(tokenOwner);
        ValidateParameters.checkForContent(spender, "spender");
        body.setSpender(spender);
        this.setBody(body);
    }
}