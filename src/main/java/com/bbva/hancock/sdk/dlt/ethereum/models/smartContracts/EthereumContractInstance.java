package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

public class EthereumContractInstance {

    private String abiName;
    private String alias;
    private String address;

    public EthereumContractInstance(String abiName, String alias, String address) {
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

}
