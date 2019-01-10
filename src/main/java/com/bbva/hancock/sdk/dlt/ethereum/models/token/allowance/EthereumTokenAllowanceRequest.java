package com.bbva.hancock.sdk.dlt.ethereum.models.token.allowance;

import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenBody;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenRequest;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.util.ValidateParameters;

import java.io.Serializable;

public class EthereumTokenAllowanceRequest extends EthereumTokenRequest implements Serializable {

    private static final long serialVersionUID = 5725499908904163332L;

    public EthereumTokenAllowanceRequest() {
    }

    public EthereumTokenAllowanceRequest(final String from, final String tokenOwner, final String spender, final String addressOrAlias) throws HancockException {
        super(addressOrAlias, "tokenAllowance");
        final EthereumTokenBody body = new EthereumTokenBody(from);
        ValidateParameters.checkForContent(tokenOwner, "tokenOwner");
        body.setTokenOwner(tokenOwner);
        ValidateParameters.checkForContent(spender, "spender");
        body.setSpender(spender);
        this.setBody(body);
    }
    
}
