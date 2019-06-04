package com.bbva.hancock.sdk.models.protocol;

import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.util.ValidateParameters;

import java.io.Serializable;
import java.math.BigInteger;

class HancockProtocolEncodeBody implements Serializable {

    private static final long serialVersionUID = 3868153081827902016L;
    public String value;
    public String data;
    public String to;

    public HancockProtocolEncodeBody() {

    }

    public HancockProtocolEncodeBody(final BigInteger value, final String to, final String data) throws HancockException {
        ValidateParameters.checkForContent(value.toString(), "value");
        this.value = value.toString();
        ValidateParameters.checkForContent(to, "to");
        this.to = to;
        this.data = data;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(final String to) {
        this.to = to;
    }
}

public class HancockProtocolEncodeRequest implements Serializable {
    private static final long serialVersionUID = 801456988162358125L;
    private HancockProtocolAction action;
    private HancockProtocolEncodeBody body;
    private HancockProtocolDlt dlt;

    public HancockProtocolEncodeRequest() {

    }

    public HancockProtocolEncodeRequest(final HancockProtocolAction action, final BigInteger value, final String to, final String data, final HancockProtocolDlt dlt) throws HancockException {
        this.action = action;
        body = new HancockProtocolEncodeBody(value, to, data);
        this.dlt = dlt;
    }

    public HancockProtocolAction getAction() {
        return action;
    }

    public void setAction(final HancockProtocolAction action) {
        this.action = action;
    }

    public HancockProtocolEncodeBody getBody() {
        return body;
    }

    public void setBody(final HancockProtocolEncodeBody body) {
        this.body = body;
    }

    public HancockProtocolDlt getDlt() {
        return dlt;
    }

    public void setDlt(final HancockProtocolDlt dlt) {
        this.dlt = dlt;
    }
}
