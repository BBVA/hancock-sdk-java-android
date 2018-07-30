package com.bbva.hancock.sdk.exception;

import okhttp3.internal.http2.ErrorCode;

public class HancockException extends Exception {

  private static final long serialVersionUID = 1L;
  
  private final ErrorCode error;
  private final String internalError;
  private final String extendedMessage;

  public HancockException(ErrorCode error) {
    super();
    this.error = error;
    this.internalError = "";
    this.extendedMessage = "";
  }

  public HancockException(String message, String extendedMessage, String internalError, Throwable cause, ErrorCode error) {
    super(message, cause);
    this.error = error;
    this.internalError = internalError;
    this.extendedMessage = extendedMessage;
  }

  public HancockException(String message, String extendedMessage, String internalError, ErrorCode error) {
    super(message);
    this.error = error;
    this.internalError = internalError;
    this.extendedMessage = extendedMessage;
  }

  public HancockException(Throwable cause, String extendedMessage, String internalError, ErrorCode error) {
    super(cause);
    this.error = error;
    this.internalError = internalError;
    this.extendedMessage = extendedMessage;
  }
  
  public ErrorCode getError() {
    return this.error;
  }
  
  public String getInternalError() {
    return this.internalError;
  }
  
  public String getExtendedMessage() {
    return this.extendedMessage;
  }
}