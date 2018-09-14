package com.bbva.hancock.sdk.models;

import java.util.ArrayList;

public class HancockSocketRequest {

    private String kind;
    private ArrayList<String> body;

    public HancockSocketRequest(String kind, ArrayList<String> body) {
        this.kind = kind;
        this.body = body;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public ArrayList<String> getBody() {
        return body;
    }

    public void setBody(ArrayList<String> body) {
        this.body = body;
    }
}
