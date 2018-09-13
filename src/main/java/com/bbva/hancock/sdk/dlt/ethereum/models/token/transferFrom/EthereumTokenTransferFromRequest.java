package com.bbva.hancock.sdk.dlt.ethereum.models.token.transferFrom;

import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenBody;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.transfer.EthereumTokenTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.util.ValidateParameters;
import com.bbva.hancock.sdk.exception.HancockException;

public class EthereumTokenTransferFromRequest extends EthereumTokenRequest {

    public EthereumTokenTransferFromRequest(String from, String sender, String to, String value, String addressOrAlias) throws HancockException {
        super(addressOrAlias, "tokenTransferFrom");
        EthereumTokenBody body = new EthereumTokenBody(from, to, value);
        ValidateParameters.checkForContent(sender, "sender");
        body.setSender(sender);
        this.setBody(body);
    }
}