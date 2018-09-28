package com.bbva.hancock.sdk.models.protocol;

import java.math.BigInteger;

import com.bbva.hancock.sdk.util.ValidateParameters;
import com.bbva.hancock.sdk.exception.HancockException;

class HancockProtocolEncodeBody{
    public String value;
    public String data;
    public String to;

    public HancockProtocolEncodeBody(BigInteger value, String to, String data) throws HancockException {
        ValidateParameters.checkForContent(value.toString(), "value");
        this.value = value.toString();
        ValidateParameters.checkForContent(to, "to");
        this.to = to;
        this.data = data;
    }
}

public class HancockProtocolEncodeRequest {
    private HancockProtocolAction action;
    private HancockProtocolEncodeBody body;
    private HancockProtocolDlt dlt;

    public HancockProtocolEncodeRequest(HancockProtocolAction action, BigInteger value, String to, String data, HancockProtocolDlt dlt) throws HancockException{
        this.action = action;
        this.body = new HancockProtocolEncodeBody(value, to, data);
        this.dlt = dlt;

    }
}
