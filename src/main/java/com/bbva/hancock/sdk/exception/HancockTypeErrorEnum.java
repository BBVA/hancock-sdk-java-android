package com.bbva.hancock.sdk.exception;

public enum HancockTypeErrorEnum {

    ERROR_API("SDKAPI_"),
    ERROR_INTERNAL("SDKINT_");

    private String type;

    private static final long serialVersionUID = 1L;

    private HancockTypeErrorEnum(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

}
