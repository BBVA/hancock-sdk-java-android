package com.bbva.hancock.sdk.models;

import java.io.Serializable;
import java.util.ArrayList;

public class HancockSocketRequest implements Serializable {

    private static final long serialVersionUID = 2632420954727518115L;
    private String kind;
    private ArrayList<String> body;

    public HancockSocketRequest() {
    }

    public HancockSocketRequest(final String kind, final ArrayList<String> body) {
        this.kind = kind;
        this.body = body;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(final String kind) {
        this.kind = kind;
    }

    public ArrayList<String> getBody() {
        return body;
    }

    public void setBody(final ArrayList<String> body) {
        this.body = body;
    }
}
