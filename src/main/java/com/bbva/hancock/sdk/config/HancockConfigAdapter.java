package com.bbva.hancock.sdk.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HancockConfigAdapter implements Serializable {

    private String host;
    private String base;
    private int port;
    private Map<String, String> resources;

    public HancockConfigAdapter() {}

    public HancockConfigAdapter(String host, String base, int port, Map<String, String> resources) {
        this.host = host;
        this.base = base;
        this.port = port;
        this.resources = resources;
    }

    public HancockConfigAdapter(HancockConfigAdapter adapter) {
        this.host = adapter.getHost();
        this.base = adapter.getBase();
        this.port = adapter.getPort();
        this.resources = new HashMap<String, String>(adapter.getResources());
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Map<String, String> getResources() {
        return resources;
    }

    public void setResources(Map<String, String> resources) {
        this.resources = resources;
    }

}
