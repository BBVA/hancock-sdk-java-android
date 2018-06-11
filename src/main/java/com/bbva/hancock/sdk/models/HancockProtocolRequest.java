package com.bbva.hancock.sdk.models;

public class HancockProtocolRequest {
    private String code;

    public HancockProtocolRequest(String code){
        this.code = code;
    }
    
	public String getCode() {
		return code;
    }
    
	public void setCode(String code) {
		this.code = code;
	}


}
