package com.bbva.hancock.sdk.models.socket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HancockSocketRequest implements Serializable {

    private static final long serialVersionUID = 2632420954727518115L;
    private String kind;
    private List<String> body;

    public HancockSocketRequest() {
    }

    public HancockSocketRequest(final String kind, final List<String> body) {
        this.kind = kind;
        this.body = body;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(final String kind) {
        this.kind = kind;
    }

    public List<String> getBody() {
        return body;
    }

    public void setBody(final ArrayList<String> body) {
        this.body = body;
    }
}
