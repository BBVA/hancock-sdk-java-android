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

    public static class Builder {

        private final String env;
        private String nodeHost;
        private int nodePort;

        public Builder(String env) {
            this.env = env;
        }

        public Builder withNode(String host, int port) {
            this.nodeHost = host;
            this.nodePort = port;
            return this;
        }

        public HancockConfig build() {

            HancockConfig config = new HancockConfig();

            config.setEnv(this.env);

            if (this.nodeHost != null) {
                HancockConfigNode configNode = new HancockConfigNode();
                configNode.setHost(this.nodeHost);
                configNode.setPort(this.nodePort);

                config.setNode(configNode);
            }

            return config;
        }


    }
}
