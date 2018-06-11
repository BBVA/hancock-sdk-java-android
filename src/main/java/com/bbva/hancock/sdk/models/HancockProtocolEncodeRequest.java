package com.bbva.hancock.sdk.models;

import java.math.BigInteger;

class HancockProtocolEncodeBody{
    public String value;
    public String data;
    public String to;

    public HancockProtocolEncodeBody(BigInteger value, String to, String data) {
        this.value = value.toString();
        this.to = to;
        this.data = data;
    }
}

public class HancockProtocolEncodeRequest {
    private HancockProtocolAction action;
    private HancockProtocolEncodeBody body;
    private HancockProtocolDlt dlt;

    public HancockProtocolEncodeRequest(HancockProtocolAction action, BigInteger value, String to, String data, HancockProtocolDlt dlt){
        this.action = action;
        this.body = new HancockProtocolEncodeBody(value, to, data);
        this.dlt = dlt;

    }
}
