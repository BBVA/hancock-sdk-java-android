package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import org.web3j.protocol.core.methods.response.AbiDefinition;

import java.io.Serializable;
import java.util.ArrayList;

public class EthereumRegisterRequest implements Serializable {

    private static final long serialVersionUID = -1815399594736850037L;

    private String address;
    private String alias;
    private ArrayList<AbiDefinition> abi;

    public EthereumRegisterRequest() {

    }

    public EthereumRegisterRequest(final String address, final String alias, final ArrayList<AbiDefinition> abi) {
        this.address = address;
        this.alias = alias;
        this.abi = abi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public ArrayList<AbiDefinition> getAbi() {
        return abi;
    }

    public void setAbi(final ArrayList<AbiDefinition> abi) {
        this.abi = abi;
    }

}
