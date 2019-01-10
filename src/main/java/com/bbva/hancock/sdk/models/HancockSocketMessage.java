package com.bbva.hancock.sdk.models;

import java.io.Serializable;

public class HancockSocketMessage implements Serializable {

    private static final long serialVersionUID = -5868311404867886960L;
    private String kind;
    private Object body;
    private String matchedAddress;

    public HancockSocketMessage() {
    }

    public HancockSocketMessage(final String kind, final Object body, final String matchedAddress) {
        this.kind = kind;
        this.body = body;
        this.matchedAddress = matchedAddress;
    }

    public String getKind() {
        return kind;
    }

    public Object getBody() {
        return body;
    }

    public String getMatchedAddress() {
        return matchedAddress;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public void setMatchedAddress(String matchedAddress) {
        this.matchedAddress = matchedAddress;
    }
}
