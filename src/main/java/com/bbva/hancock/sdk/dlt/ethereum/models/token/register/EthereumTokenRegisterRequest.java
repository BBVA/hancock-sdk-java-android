package com.bbva.hancock.sdk.dlt.ethereum.models.token.register;

import com.bbva.hancock.sdk.dlt.ethereum.models.util.ValidateParameters;
import com.bbva.hancock.sdk.exception.HancockException;

public class EthereumTokenRegisterRequest {
    public String alias;
    public String address;

    public EthereumTokenRegisterRequest(String alias, String address) throws HancockException {
        ValidateParameters.checkForContent(alias, "alias");
        this.alias = alias;
        ValidateParameters.checkForContent(address, "address");
        ValidateParameters.checkAddress(address);
        this.address = address;
    }
}
