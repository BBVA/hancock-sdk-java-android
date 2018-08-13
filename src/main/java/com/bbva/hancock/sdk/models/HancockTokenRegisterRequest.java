package com.bbva.hancock.sdk.models;

import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.models.util.ValidateParameters;

public class HancockTokenRegisterRequest {
    public String alias;
    public String address;

    public HancockTokenRegisterRequest(String alias, String address) throws HancockException {
        ValidateParameters.checkForContent(alias, "alias");
        this.alias = alias;
        ValidateParameters.checkForContent(address, "address");
        ValidateParameters.checkAddress(address);
        this.address = address;
    }
}
