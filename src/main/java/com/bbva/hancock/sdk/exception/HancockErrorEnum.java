package com.bbva.hancock.sdk.exception;

public enum HancockErrorEnum {


    ERROR_WALLET("Error generating wallet"),

    ERROR_API("Error calling Api"),

    ERROR_PARAMETER("Empty parameters"),

    ERROR_FORMAT("Addres invalid format"),

    ERROR_NOKEY_NOPROVIDER("No key nor provider"),

    ERROR_SOCKET("Error with the Socket");

    private static final long serialVersionUID = 1L;

    private String message;

    private HancockErrorEnum(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

}
