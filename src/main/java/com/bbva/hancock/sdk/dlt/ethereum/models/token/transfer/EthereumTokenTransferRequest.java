package com.bbva.hancock.sdk.dlt.ethereum.models.token.transfer;

import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenBody;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenRequest;
import com.bbva.hancock.sdk.exception.HancockException;

public class EthereumTokenTransferRequest extends EthereumTokenRequest {

    public EthereumTokenTransferRequest(String from, String to, String value, String addressOrAlias) throws HancockException {
        super(addressOrAlias, "tokenTransfer");
        EthereumTokenBody body = new EthereumTokenBody(from, to, value);
        this.setBody(body);
    }

}