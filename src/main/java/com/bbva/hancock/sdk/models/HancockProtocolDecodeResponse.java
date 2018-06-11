package com.bbva.hancock.sdk.models;

import java.math.BigInteger;

class HancockProtocolDecodeResponseResult {
    public int code;
    public String description;
}

class HancockProtocolDecodeResponseData {
    public HancockProtocolAction action;
    public HancockProtocolDecodeResponseBody body;
    public HancockProtocolDlt dlt;
}

class HancockProtocolDecodeResponseBody{
    public BigInteger value;
    public String data;
    public String to;
}

public class HancockProtocolDecodeResponse {
    private HancockProtocolDecodeResponseResult result;
    private HancockProtocolDecodeResponseData data;

	public HancockProtocolDecodeResponseResult getResult() {
		return result;
    }
    
	public HancockProtocolDecodeResponseData getData() {
		return data;
    }
    
    public HancockProtocolAction getAction(){
        return getData().action;
    }
    
    public HancockProtocolDlt getDlt(){
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
