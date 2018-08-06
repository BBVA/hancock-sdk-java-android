package com.bbva.hancock.sdk.exception;

public enum HancockErrorEnum {

    ERROR_WALLET("Error generating wallet"),
    ERROR_CHECK("Error check Json"),
    ERROR_HTTP("Error calling Api");
    
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
