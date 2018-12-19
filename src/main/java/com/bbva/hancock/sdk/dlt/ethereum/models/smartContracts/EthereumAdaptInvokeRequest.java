package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import java.io.Serializable;
import java.util.ArrayList;

public class EthereumAdaptInvokeRequest implements Serializable {

    private static final long serialVersionUID = 4664097077402186422L;
    private String method;
    private String from;
    private ArrayList<String> params;
    private String action;

    public EthereumAdaptInvokeRequest() {
    }

    public EthereumAdaptInvokeRequest(final String method, final String from, final ArrayList<String> params, final String action) {
        this.method = method;
        this.from = from;
        this.params = params;
        this.action = action;
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

}
