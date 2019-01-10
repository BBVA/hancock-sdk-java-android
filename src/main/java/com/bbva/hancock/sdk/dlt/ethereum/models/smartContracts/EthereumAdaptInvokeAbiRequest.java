package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import org.web3j.protocol.core.methods.response.AbiDefinition;

import java.io.Serializable;
import java.util.ArrayList;

public class EthereumAdaptInvokeAbiRequest implements Serializable {

    private static final long serialVersionUID = 928894613662366476L;
    private String method;
    private String from;
    private ArrayList<String> params;
    private String action;
    private String to;
    private ArrayList<AbiDefinition> abi;

    public EthereumAdaptInvokeAbiRequest() {
    }

    public EthereumAdaptInvokeAbiRequest(final String method, final String from, final ArrayList<String> params, final String action, final String to, final ArrayList<AbiDefinition> abi) {
        this.method = method;
        this.from = from;
        this.params = params;
        this.action = action;
        this.to = to;
        this.abi = abi;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public void setParams(final ArrayList<String> params) {
        this.params = params;
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public String getTo() {
        return to;
    }

    public void setTo(final String to) {
        this.to = to;
    }

    public ArrayList<AbiDefinition> getAbi() {
        return abi;
    }

    public void setAbi(final ArrayList<AbiDefinition> abi) {
        this.abi = abi;
    }


}
