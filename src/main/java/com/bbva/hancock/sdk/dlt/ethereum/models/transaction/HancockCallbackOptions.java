package com.bbva.hancock.sdk.dlt.ethereum.models.transaction;

public class HancockCallbackOptions {

    private String backUrl;
    private String requestId;

    public HancockCallbackOptions(String backUrl, String requestId) {
        this.backUrl = backUrl;
        this.requestId = requestId;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
