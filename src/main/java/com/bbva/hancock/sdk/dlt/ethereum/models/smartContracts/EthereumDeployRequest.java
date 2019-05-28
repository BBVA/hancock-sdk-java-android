package com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts;

import java.io.Serializable;
import java.util.List;

public class EthereumDeployRequest implements Serializable {

    private static final long serialVersionUID = -1969223599062759995L;

    private String urlBase;
    private String method;
    private List<String> params;
    private String from;

    public EthereumDeployRequest(String urlBase, String method, List<String> params, String from) {
        this.urlBase = urlBase;
        this.method = method;
        this.params = params;
        this.from = from;
    }

    public String getUrlBase() {
        return urlBase;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
