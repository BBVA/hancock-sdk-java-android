package com.bbva.hancock.sdk.models;

import java.math.BigInteger;

class HancockProtocolResponseResult {
    public int code;
    public String description;
}

class HancockProtocolResponseData {
    public String action;
    public HancockProtocolResponseBody body;
    public String dlt;


}

class HancockProtocolResponseBody{
    public BigInteger value;
    public String data;
    public String to;
}

public class HancockProtocolResponse {
    private HancockProtocolResponseResult result;
    private HancockProtocolResponseData data;

	public HancockProtocolResponseResult getResult() {
		return result;
    }
    
	public HancockProtocolResponseData getData() {
		return data;
    }
    
    public String getAction(){
        return getData().action;
    }
    
    public String getDlt(){
        return getData().dlt;
    }
    
    public BigInteger getValue(){
        return getData().body.value;
    }
    
    public String getBodyData(){
        return getData().body.data;
    }
    
    public String getTo(){
        return getData().body.to;
    }
}
