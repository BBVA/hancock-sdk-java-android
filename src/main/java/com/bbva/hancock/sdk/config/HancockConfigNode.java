package com.bbva.hancock.sdk.config;

import java.io.Serializable;

public class HancockConfigNode implements Serializable {

    private static final long serialVersionUID = 8595675037252132505L;
    private String host;
    private int port;

    public HancockConfigNode() {
    }

    public HancockConfigNode(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    public HancockConfigNode(final HancockConfigNode node) {
        this.host = node.getHost();
        this.port = node.getPort();
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }
}
