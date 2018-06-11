package com.bbva.hancock.sdk.models;

public class HancockProtocolDecodeRequest {
    private String code;

    public HancockProtocolDecodeRequest(String code){
        this.code = code;
    }
    
	public String getCode() {
		return code;
    }
    
	public void setCode(String code) {
		this.code = code;
	}


}
