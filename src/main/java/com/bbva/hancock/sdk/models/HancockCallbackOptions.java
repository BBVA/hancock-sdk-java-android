package com.bbva.hancock.sdk.models;

import java.io.Serializable;

public class HancockCallbackOptions implements Serializable {

    private static final long serialVersionUID = -3864572828207391441L;
    private String backUrl;
    private String requestId;

    public HancockCallbackOptions() {

    }

    public HancockCallbackOptions(final String backUrl, final String requestId) {
        this.backUrl = backUrl;
        this.requestId = requestId;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(final String backUrl) {
        this.backUrl = backUrl;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }

}
