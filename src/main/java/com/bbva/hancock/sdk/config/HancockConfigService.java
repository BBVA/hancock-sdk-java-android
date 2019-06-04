package com.bbva.hancock.sdk.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HancockConfigService implements Serializable {

    private static final long serialVersionUID = 4385139048947826920L;
    private String host;
    private String base;
    private int port;
    private Map<String, String> resources;

    public HancockConfigService() {
    }

    public HancockConfigService(final String host, final String base, final int port, final Map<String, String> resources) {
        this.host = host;
        this.base = base;
        this.port = port;
        this.resources = resources;
    }

    public HancockConfigService(final HancockConfigService service) {
        host = service.getHost();
        base = service.getBase();
        port = service.getPort();
        resources = new HashMap(service.getResources());
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getBase() {
        return base;
    }

    public void setBase(final String base) {
        this.base = base;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public Map<String, String> getResources() {
        return resources;
    }

    public void setResources(final Map<String, String> resources) {
        this.resources = resources;
    }

}
