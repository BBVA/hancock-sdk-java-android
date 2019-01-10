package com.bbva.hancock.sdk.models.protocol;

import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.util.ValidateParameters;

import java.io.Serializable;

public class HancockProtocolDecodeRequest implements Serializable {
    private static final long serialVersionUID = -2194499566349749962L;
    private String code;

    public HancockProtocolDecodeRequest() {
    }

    public HancockProtocolDecodeRequest(final String code) throws HancockException {
        ValidateParameters.checkForContent(code, "code");
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }


}
