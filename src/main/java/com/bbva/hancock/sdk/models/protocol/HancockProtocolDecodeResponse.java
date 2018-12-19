package com.bbva.hancock.sdk.models.protocol;

import java.io.Serializable;
import java.math.BigInteger;

class HancockProtocolDecodeResponseResult implements Serializable {
    private static final long serialVersionUID = 9029479039564396068L;
    public int code;
    public String description;

    public HancockProtocolDecodeResponseResult() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}

class HancockProtocolDecodeResponseData implements Serializable {
    private static final long serialVersionUID = 1272447018585890002L;
    public HancockProtocolAction action;
    public HancockProtocolDecodeResponseBody body;
    public HancockProtocolDlt dlt;

    public HancockProtocolDecodeResponseData() {
    }

    public HancockProtocolAction getAction() {
        return action;
    }

    public void setAction(final HancockProtocolAction action) {
        this.action = action;
    }

    public HancockProtocolDecodeResponseBody getBody() {
        return body;
    }

    public void setBody(final HancockProtocolDecodeResponseBody body) {
        this.body = body;
    }

    public HancockProtocolDlt getDlt() {
        return dlt;
    }

    public void setDlt(final HancockProtocolDlt dlt) {
        this.dlt = dlt;
    }
}

class HancockProtocolDecodeResponseBody implements Serializable {
    private static final long serialVersionUID = -3214984967495484686L;
    public BigInteger value;
    public String data;
    public String to;

    public HancockProtocolDecodeResponseBody() {
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(final BigInteger value) {
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

public class HancockProtocolDecodeResponse implements Serializable {
    private static final long serialVersionUID = -7071167559001183366L;
    private HancockProtocolDecodeResponseResult result;
    private HancockProtocolDecodeResponseData data;

    public HancockProtocolDecodeResponse() {
    }

    public HancockProtocolDecodeResponseResult getResult() {
        return result;
    }

    public HancockProtocolDecodeResponseData getData() {
        return data;
    }

    public HancockProtocolAction getAction() {
        return getData().action;
    }

    public HancockProtocolDlt getDlt() {
        return getData().dlt;
    }

    public BigInteger getValue() {
        return getData().body.value;
    }

    public String getBodyData() {
        return getData().body.data;
    }

    public String getTo() {
        return getData().body.to;
    }

    public void setResult(final HancockProtocolDecodeResponseResult result) {
        this.result = result;
    }

    public void setData(final HancockProtocolDecodeResponseData data) {
        this.data = data;
    }
}
