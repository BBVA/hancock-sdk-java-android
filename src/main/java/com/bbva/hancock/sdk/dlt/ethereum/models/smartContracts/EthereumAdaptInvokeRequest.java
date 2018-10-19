package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import java.util.ArrayList;

public class EthereumAdaptInvokeRequest {

    private String method;
    private String from;
    private ArrayList<String> params;
    private String action;

    public EthereumAdaptInvokeRequest(String method, String from, ArrayList<String> params, String action) {
        this.method = method;
        this.from = from;
        this.params = params;
        this.action = action;
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
}
