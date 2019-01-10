package com.bbva.hancock.sdk.dlt.ethereum.models.token.transfer;

import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenBody;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenRequest;
import com.bbva.hancock.sdk.exception.HancockException;

import java.io.Serializable;

public class EthereumTokenTransferRequest extends EthereumTokenRequest implements Serializable {

    private static final long serialVersionUID = 39520445275788329L;

    public EthereumTokenTransferRequest() {
    }

    public EthereumTokenTransferRequest(final String from, final String to, final String value, final String addressOrAlias) throws HancockException {
        super(addressOrAlias, "tokenTransfer");
        final EthereumTokenBody body = new EthereumTokenBody(from, to, value);
        this.setBody(body);
    }

}
