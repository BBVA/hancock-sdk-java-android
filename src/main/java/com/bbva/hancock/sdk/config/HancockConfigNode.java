package com.bbva.hancock.sdk.config;

import java.io.Serializable;

public class HancockConfigNode implements Serializable {

    private String host;
    private int port;

    public HancockConfigNode() {}

    public HancockConfigNode(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public HancockConfigNode(HancockConfigNode node) {
        this.host = node.getHost();
        this.port = node.getPort();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
