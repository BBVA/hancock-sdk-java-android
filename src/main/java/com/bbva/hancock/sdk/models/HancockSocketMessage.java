package com.bbva.hancock.sdk.models;

import com.google.gson.JsonObject;

import java.io.Serializable;

public class HancockSocketMessage implements Serializable {

    private static final long serialVersionUID = -5868311404867886960L;
    private String kind;
    private JsonObject body;
    private String matchedAddress;

    public HancockSocketMessage() {
    }

    public HancockSocketMessage(final String kind, final JsonObject body, final String matchedAddress) {
        this.kind = kind;
        this.body = body;
        this.matchedAddress = matchedAddress;
    }

    public String getKind() {
        return kind;
    }

    public JsonObject getBody() {
        return body;
    }

    public String getMatchedAddress() {
        return matchedAddress;
    }

    public void setKind(final String kind) {
        this.kind = kind;
    }

    public void setBody(final JsonObject body) {
        this.body = body;
    }

    public void setMatchedAddress(final String matchedAddress) {
        this.matchedAddress = matchedAddress;
    }

}
