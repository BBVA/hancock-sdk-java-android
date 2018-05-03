package com.bbva.hancock.sdk.config;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;


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

    public static HancockConfig createDefaultConfig() {

        try {

            InputStream input = new FileInputStream(new File("application.yml"));
            Yaml yaml = new Yaml();

            Map<String, Object> object = (Map<String, Object>) yaml.load(input);
            System.out.println(object);

            String env = (String) object.get("env");
            Map<String, Object> node = (Map<String, Object>) object.get("node");

            return new Builder(env)
                    .withNode((String) node.get("host"), (Integer) node.get("port"))
                    .build();

        } catch (FileNotFoundException e) {

            return new HancockConfig();

        }

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
