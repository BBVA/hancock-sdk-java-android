package com.bbva.hancock.sdk.models;

import java.math.BigInteger;

class HancockProtocolEncodeResponseResult {
    public int code;
    public String description;
}

class HancockProtocolEncodeResponseData {
    public String qrEncode;
}

public class HancockProtocolEncodeResponse {
    private HancockProtocolEncodeResponseResult result;
    private HancockProtocolEncodeResponseData data;

	public HancockProtocolEncodeResponseResult getResult() {
		return result;
    }
    
	public HancockProtocolEncodeResponseData getData() {
		return data;
    }
    
    public String getCode(){
        return getData().qrEncode;
    }
}
