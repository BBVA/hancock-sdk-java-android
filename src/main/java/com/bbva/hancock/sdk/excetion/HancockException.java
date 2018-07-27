package com.bbva.hancock.sdk.excetion;

import okhttp3.internal.http2.ErrorCode;

public class HancockException extends Exception {

  private static final long serialVersionUID = 1L;
  
  private final ErrorCode code;
  private final String internalCode;
  private final String extendedMessage;
  
  public final static String ERROR_WALLET = "Error generating wallet";

  public HancockException(ErrorCode code) {
    super();
    this.code = code;
    this.internalCode = "";
    this.extendedMessage = "";
  }

  public HancockException(String message, String extendedMessage, String internalCode, Throwable cause, ErrorCode code) {
    super(message, cause);
    this.code = code;
    this.internalCode = internalCode;
    this.extendedMessage = extendedMessage;
  }

  public HancockException(String message, String extendedMessage, String internalCode, ErrorCode code) {
    super(message);
    this.code = code;
    this.internalCode = internalCode;
    this.extendedMessage = extendedMessage;
  }

  public HancockException(Throwable cause, String extendedMessage, String internalCode, ErrorCode code) {
    super(cause);
    this.code = code;
    this.internalCode = internalCode;
    this.extendedMessage = extendedMessage;
  }
  
  public ErrorCode getCode() {
    return this.code;
  }
  
  public String getInternalCode() {
    return this.internalCode;
  }
  
  public String getExtendedMessage() {
    return this.extendedMessage;
  }
}