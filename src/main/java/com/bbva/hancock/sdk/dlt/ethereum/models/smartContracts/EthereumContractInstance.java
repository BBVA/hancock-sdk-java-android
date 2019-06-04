package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import java.io.Serializable;

public class EthereumContractInstance implements Serializable {

    private static final long serialVersionUID = -2413455745795731104L;

    private String abiName;
    private String alias;
    private String address;

    public EthereumContractInstance() {
    }

    public EthereumContractInstance(final String abiName, final String alias, final String address) {
        this.abiName = abiName;
        this.alias = alias;
        this.address = address;
    }

    public String getAbiName() {
        return abiName;
    }

    public String getAlias() {
        return alias;
    }

    public String getAddress() {
        return address;
    }

    public void setAbiName(final String abiName) {
        this.abiName = abiName;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

}
