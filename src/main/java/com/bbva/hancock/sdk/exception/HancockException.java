package com.bbva.hancock.sdk.exception;

public class HancockException extends Exception {

    private static final long serialVersionUID = 7009082313661428202L;

    private final Integer error;
    private final String internalError;
    private final String extendedMessage;
    public HancockTypeErrorEnum typeError;

    public HancockException(final HancockTypeErrorEnum typeError, final String internalError, final Integer error, final String message, final String extendedMessage, final Throwable cause) {
        super(message, cause);
        this.error = error;
        if (typeError.equals(HancockTypeErrorEnum.ERROR_API)) {
            this.internalError = typeError.getType() + internalError;
        } else {
            this.internalError = typeError.getType() + internalError;
        }
        this.extendedMessage = extendedMessage;
    }

    public HancockException(final HancockTypeErrorEnum typeError, final String internalError, final Integer error, final String message, final String extendedMessage) {
        super(message);
        this.error = error;
        if (typeError.equals(HancockTypeErrorEnum.ERROR_API)) {
            this.internalError = typeError.getType() + internalError;
        } else {
            this.internalError = typeError.getType() + internalError;
        }
        this.extendedMessage = extendedMessage;
    }

    public Integer getError() {
        return error;
    }

    public String getInternalError() {
        return internalError;
    }

    public String getExtendedMessage() {
        return extendedMessage;
    }

}
