package com.bbva.hancock.sdk.models;

class HancockTokenRegisterResponseResult {
    public int code;
    public String description;
}

public class HancockTokenRegisterResponse {
    private HancockTokenRegisterResponseResult result;

    public HancockTokenRegisterResponseResult getResult() {
        return result;
    }

}
