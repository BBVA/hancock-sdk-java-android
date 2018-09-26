package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import org.web3j.protocol.core.methods.response.AbiDefinition;

import java.util.ArrayList;

public class EthereumRegisterRequest {

    private String address;
    private String alias;
    private ArrayList<AbiDefinition> abi;

    public EthereumRegisterRequest(String address, String alias, ArrayList<AbiDefinition> abi) {
        this.address = address;
        this.alias = alias;
        this.abi = abi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ArrayList<AbiDefinition> getAbi() {
        return abi;
    }

    public void setAbi(ArrayList<AbiDefinition> abi) {
        this.abi = abi;
    }
}
