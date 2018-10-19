package com.bbva.hancock.sdk.models;

public class HancockGenericResponse {

    private Integer code;
    private String description;

    public HancockGenericResponse(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
