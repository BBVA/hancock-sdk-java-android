package com.bbva.hancock.sdk.models;

public class HancockTokenRegisterRequest {
    public String alias;
    public String address;

    public HancockTokenRegisterRequest(String alias, String address) {
        this.alias = alias;
        this.address = address;
    }
}
