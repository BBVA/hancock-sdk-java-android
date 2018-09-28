package com.bbva.hancock.sdk.models.protocol;

import com.bbva.hancock.sdk.util.ValidateParameters;
import com.bbva.hancock.sdk.exception.HancockException;

public class HancockProtocolDecodeRequest {
    private String code;

    public HancockProtocolDecodeRequest(String code) throws HancockException{
        ValidateParameters.checkForContent(code, "code");
        this.code = code;
    }
    
	public String getCode() {
		return code;
    }
    
	public void setCode(String code) {
		this.code = code;
	}


}
