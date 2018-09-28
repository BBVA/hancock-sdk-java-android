package com.bbva.hancock.sdk.models;

import java.util.ArrayList;

public class HancockSocketMessage {

    private String kind;
    private Object body;
    private String matchedAddress;

    public HancockSocketMessage(String kind, Object body, String matchedAddress) {
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
}
