package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import java.util.ArrayList;

public class EthereumAdaptInvokeAbiRequest {

    private String method;
    private String from;
    private ArrayList<String> params;
    private String action;
    private String to;
    private String abi;

    public EthereumAdaptInvokeAbiRequest(String method, String from, ArrayList<String> params, String action, String to, String abi) {
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

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTo() {
      return to;
    }

    public void setTo(String to) {
      this.to = to;
    }

    public String getAbi() {
      return abi;
    }

    public void setAbi(String abi) {
      this.abi = abi;
    }
    
}
