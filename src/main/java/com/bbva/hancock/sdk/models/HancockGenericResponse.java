package com.bbva.hancock.sdk.models;

import java.io.Serializable;

public class HancockGenericResponse implements Serializable {

    private static final long serialVersionUID = -9126991645679277308L;
    private Integer code;
    private String description;

    public HancockGenericResponse() {

    }

    public HancockGenericResponse(final Integer code, final String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

}
