package com.bbva.hancock.sdk.dlt.ethereum.models.token.transferFrom;

import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenBody;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenRequest;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.util.ValidateParameters;

import java.io.Serializable;

public class EthereumTokenTransferFromRequest extends EthereumTokenRequest implements Serializable {

    private static final long serialVersionUID = 2475053012295938078L;

    public EthereumTokenTransferFromRequest() {
    }

    public EthereumTokenTransferFromRequest(final String from, final String sender, final String to, final String value, final String addressOrAlias) throws HancockException {
        super(addressOrAlias, "tokenTransferFrom");
        final EthereumTokenBody body = new EthereumTokenBody(from, to, value);
        ValidateParameters.checkForContent(sender, "sender");
        body.setSender(sender);
        setBody(body);
    }

}
