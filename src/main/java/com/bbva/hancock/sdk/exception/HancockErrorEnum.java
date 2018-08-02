package com.bbva.hancock.sdk.exception;

public enum HancockErrorEnum {

    ERROR_WALLET("Hancock error - Error generating wallet"),
    ERROR_CHECK("Hancock error - Error check Json"),
    ERROR_HTTP("Hancock error - Error response make call");
    
    private String message;
    
    private static final long serialVersionUID = 1L;
  
    private HancockErrorEnum(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
   
}
