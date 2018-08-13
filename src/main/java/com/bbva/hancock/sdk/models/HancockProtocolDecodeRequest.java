package com.bbva.hancock.sdk.models;

import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.models.util.ValidateParameters;

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
