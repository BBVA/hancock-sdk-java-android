package com.bbva.hancock.sdk.exception;

public class HancockException extends Exception {

  private static final long serialVersionUID = 1L;
  
  private final Integer error;
  private final String internalError;
  private final String extendedMessage;
  public HancockTypeErrorEnum typeError;

  public HancockException(HancockTypeErrorEnum typeError ,String internalError, Integer error, String message, String extendedMessage, Throwable cause) {
    super(message, cause);
    this.error = error;
    if(typeError.equals(HancockTypeErrorEnum.ERROR_API)){
      this.internalError = typeError.getType()+internalError;
    }else{
      this.internalError = typeError.getType()+internalError;
    }
    this.extendedMessage = extendedMessage;
  }

  public HancockException(HancockTypeErrorEnum typeError ,String internalError, Integer error, String message, String extendedMessage) {
    super(message);
    this.error = error;
    if(typeError.equals(HancockTypeErrorEnum.ERROR_API)){
      this.internalError = typeError.getType()+internalError;
    }else{
      this.internalError = typeError.getType()+internalError;
    }
    this.extendedMessage = extendedMessage;
  }
  
  public Integer getError() {
    return this.error;
  }
  
  public String getInternalError() {
    return this.internalError;
  }
  
  public String getExtendedMessage() {
    return this.extendedMessage;
  }
}