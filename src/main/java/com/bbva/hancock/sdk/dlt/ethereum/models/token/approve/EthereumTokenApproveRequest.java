package com.bbva.hancock.sdk.dlt.ethereum.models.token.approve;

import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenBody;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.util.ValidateParameters;
import com.bbva.hancock.sdk.exception.HancockException;

public class EthereumTokenApproveRequest extends EthereumTokenRequest {

    public EthereumTokenApproveRequest(String from, String spender, String value, String addressOrAlias) throws HancockException {
        super(addressOrAlias, "tokenApprove");
        EthereumTokenBody body = new EthereumTokenBody(from, value);
        ValidateParameters.checkForContent(spender, "spender");
        body.setSpender(spender);
        this.setBody(body);
    }
}