package com.bbva.hancock.sdk.models.protocol;

import java.io.Serializable;

class HancockProtocolEncodeResponseResult implements Serializable {
    private static final long serialVersionUID = -7632393105088718730L;
    public int code;
    public String description;

    public HancockProtocolEncodeResponseResult() {
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

class HancockProtocolEncodeResponseData implements Serializable {
    private static final long serialVersionUID = 4628668244668947789L;
    public String qrEncode;

    public HancockProtocolEncodeResponseData() {
    }

    public String getQrEncode() {
        return qrEncode;
    }

    public void setQrEncode(final String qrEncode) {
        this.qrEncode = qrEncode;
    }
}

public class HancockProtocolEncodeResponse implements Serializable {
    private static final long serialVersionUID = 8701684638816034895L;
    private HancockProtocolEncodeResponseResult result;
    private HancockProtocolEncodeResponseData data;

    public HancockProtocolEncodeResponse() {
    }

    public HancockProtocolEncodeResponseResult getResult() {
        return result;
    }

    public HancockProtocolEncodeResponseData getData() {
        return data;
    }

    public String getCode() {
        return getData().qrEncode;
    }

    public void setResult(final HancockProtocolEncodeResponseResult result) {
        this.result = result;
    }

    public void setData(final HancockProtocolEncodeResponseData data) {
        this.data = data;
    }
}
