package com.bbva.hancock.sdk.config;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;


public class HancockConfig implements Serializable {

    private String env;
    public HancockConfigNode node;
    public HancockConfigAdapter adapter;

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

    public HancockConfigAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(HancockConfigAdapter adapter) {
        this.adapter = adapter;
    }

    public static class Builder {

        private String env;
        private HancockConfigNode node;
        private HancockConfigAdapter adapter;

        public Builder() {
            this.fromConfigFile();
        }

        public Builder withEnv(String env) {
            this.env = env;
            return this;
        }

        public Builder withNode(String host, int port) {

            if (this.node == null) {
                this.node = new HancockConfigNode();
            }

            this.node.setHost(host);
            this.node.setPort(port);

            return this;
        }

        public Builder withAdapter(String host, String base, int port) {

            if (this.adapter == null) {
                this.adapter = new HancockConfigAdapter();
            }

            this.adapter.setHost(host);
            this.adapter.setBase(base);
            this.adapter.setPort(port);

            return this;
        }

        public HancockConfig build() {

            HancockConfig config = new HancockConfig();

            config.setEnv(this.env);

            if (this.node != null) {
                config.setNode(this.node);
            }

            if (this.adapter != null) {
                config.setAdapter(this.adapter);
            }

            return config;
        }


        private Builder withAdapter(String host, String base, int port, Map<String, String> resources) {
            this.adapter = new HancockConfigAdapter(host, base, port, resources);
            return this;
        }

        private Builder fromConfigFile() {

            InputStream input = getClass().getClassLoader().getResourceAsStream("application.yml");
            Yaml yaml = new Yaml();

            Map<String, Object> object = (Map<String, Object>) yaml.load(input);
            System.out.println(object);

            String env = (String) object.get("env");
            Map<String, Object> node = (Map<String, Object>) object.get("node");
            Map<String, Object> adapter = (Map<String, Object>) object.get("adapter");

            this.withEnv(env);
            this.withAdapter((String) adapter.get("host"), (String) adapter.get("base"), (int) adapter.get("port"), (Map<String, String>) adapter.get("resources"));
            this.withNode((String) node.get("host"), (int) node.get("port"));
            return this;

        }


    }
}
