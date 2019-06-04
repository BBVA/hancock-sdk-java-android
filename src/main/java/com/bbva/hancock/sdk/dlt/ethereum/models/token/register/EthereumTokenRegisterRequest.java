package com.bbva.hancock.sdk.dlt.ethereum.models.token.register;

import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.util.ValidateParameters;

import java.io.Serializable;

public class EthereumTokenRegisterRequest implements Serializable {

    private static final long serialVersionUID = 6405850257253978124L;
    private String alias;
    private String address;

    public EthereumTokenRegisterRequest() {
    }

    public EthereumTokenRegisterRequest(final String alias, final String address) throws HancockException {
        ValidateParameters.checkForContent(alias, "alias");
        this.alias = alias;
        ValidateParameters.checkForContent(address, "address");
        ValidateParameters.checkAddress(address);
        this.address = address;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }
}
