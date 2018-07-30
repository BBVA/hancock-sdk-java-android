package com.bbva.hancock.sdk.exception;

public enum HancockErrorEnum {

    ERROR_WALLET("Error generating wallet"),
    ERROR_CHECK("Error check Json"),
    ERROR_HTTP("Error response make call");
    
    private HancockErrorEnum(String system) {
        this.system = system;
    }
    public String getSystem() {
        return system;
    }
    public void setSystem(String system) {
        this.system = system;
    }
    private String system;
    
    private static final long serialVersionUID = 1L;
   
}
