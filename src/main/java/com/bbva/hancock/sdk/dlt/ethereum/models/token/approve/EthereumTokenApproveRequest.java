package com.bbva.hancock.sdk.dlt.ethereum.models.token.approve;

import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenBody;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenRequest;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.util.ValidateParameters;

import java.io.Serializable;

public class EthereumTokenApproveRequest extends EthereumTokenRequest implements Serializable {

    private static final long serialVersionUID = -3563724753862023933L;

    public EthereumTokenApproveRequest() {
    }

    public EthereumTokenApproveRequest(final String from, final String spender, final String value, final String addressOrAlias) throws HancockException {
        super(addressOrAlias, "tokenApprove");
        final EthereumTokenBody body = new EthereumTokenBody(from, value);
        ValidateParameters.checkForContent(spender, "spender");
        body.setSpender(spender);
        setBody(body);
    }

}
