package com.bbva.hancock.sdk.dlt.ethereum.models;

public class HancockGenericResponse {

    private Integer code;
    private String description;

    public HancockGenericResponse(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
