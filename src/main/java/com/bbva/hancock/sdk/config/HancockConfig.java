package com.bbva.hancock.sdk.config;

import java.io.Serializable;


public class HancockConfig implements Serializable {

    private String env;
    public HancockConfigNode node;

    public HancockConfig() { }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public HancockConfigNode getNode() {
        return node;
    }

    public void setNode(HancockConfigNode node) {
        this.node = node;
    }
}
