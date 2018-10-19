package com.bbva.hancock.sdk.config;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

@SuppressWarnings("unchecked")
public class HancockConfig implements Serializable {

    private String env;
    public HancockConfigNode node;
    public HancockConfigService adapter;
    public HancockConfigService wallet;
    public HancockConfigService broker;

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

    public HancockConfigService getAdapter() {
        return adapter;
    }

    public void setAdapter(HancockConfigService adapter) {
        this.adapter = adapter;
    }

    public HancockConfigService getWallet() {
        return wallet;
    }

    public void setWallet(HancockConfigService wallet) {
        this.wallet = wallet;
    }

    public HancockConfigService getBroker() {
        return broker;
    }

    public void setBroker(HancockConfigService broker) {
        this.broker = broker;
    }

    public static class Builder {

        private String env;
        private HancockConfigNode node;
        private HancockConfigService adapter;
        private HancockConfigService wallet;
        private HancockConfigService broker;

        /**
         * Builder for HancockConfig
         */
        public Builder() {
            this.fromConfigFile();
        }

        /**
         * Configuration of environment
         * @param env Environment name
         * @return Builder
         */
        public Builder withEnv(String env) {
            this.env = env;
            return this;
        }

        /**
         * Configuration of DLT node to connect to
         * @param host Host of the node
         * @param port Port of the node
         * @return Builder
         */
        public Builder withNode(String host, int port) {

            if (this.node == null) {
                this.node = new HancockConfigNode();
            }

            this.node.setHost(host);
            this.node.setPort(port);

            return this;
        }

        /**
         * Configuration of Hancock's Adapter
         * @param host Host of Adapter
         * @param base Base String to build url endpoints of Adapter
         * @param port Port of Adapter
         * @return Builder
         */
        public Builder withAdapter(String host, String base, int port) {

            if (this.adapter == null) {
                this.adapter = new HancockConfigService();
            }

            this.adapter.setHost(host);
            this.adapter.setBase(base);
            this.adapter.setPort(port);

            return this;
        }

        /**
         * Configuration of Hancock's Wallet
         * @param host Host of Wallet
         * @param base Base String to build url endpoints of Wallet
         * @param port Port of Wallet
         * @return Builder
         */
        public Builder withWallet(String host, String base, int port) {

            if (this.wallet == null) {
                this.wallet = new HancockConfigService();
            }

            this.wallet.setHost(host);
            this.wallet.setBase(base);
            this.wallet.setPort(port);

            return this;
        }

        /**
         * Configuration of Hancock's Broker
         * @param host Host of Broker
         * @param base Base String to build url endpoints of Broker
         * @param port Port of Broker
         * @return Builder
         */
        public Builder withBroker(String host, String base, int port) {

            if (this.broker == null) {
                this.broker = new HancockConfigService();
            }

            this.broker.setHost(host);
            this.broker.setBase(base);
            this.broker.setPort(port);

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

            if (this.wallet != null) {
                config.setWallet(this.wallet);
            }

            if (this.broker != null) {
                config.setBroker(this.broker);
            }

            return config;
        }


        protected Builder withAdapter(String host, String base, int port, Map<String, String> resources) {
            this.adapter = new HancockConfigService(host, base, port, resources);
            return this;
        }

        protected Builder withWallet(String host, String base, int port, Map<String, String> resources) {
            this.wallet = new HancockConfigService(host, base, port, resources);
            return this;
        }

        protected Builder withBroker(String host, String base, int port, Map<String, String> resources) {
            this.broker = new HancockConfigService(host, base, port, resources);
            return this;
        }

        protected Builder fromConfigFile() {

            InputStream input = getClass().getClassLoader().getResourceAsStream("application.yml");
            Yaml yaml = new Yaml();

            Map<String, Object> object = (Map<String, Object>) yaml.load(input);

            String env = (String) object.get("env");
            Map<String, Object> node = (Map<String, Object>) object.get("node");
            Map<String, Object> adapter = (Map<String, Object>) object.get("adapter");
            Map<String, Object> wallet = (Map<String, Object>) object.get("wallet");
            Map<String, Object> broker = (Map<String, Object>) object.get("broker");

            this.withEnv(env);
            this.withAdapter((String) adapter.get("host"), (String) adapter.get("base"), (int) adapter.get("port"), (Map<String, String>) adapter.get("resources"));
            this.withWallet((String) wallet.get("host"), (String) wallet.get("base"), (int) wallet.get("port"), (Map<String, String>) wallet.get("resources"));
            this.withBroker((String) broker.get("host"), (String) broker.get("base"), (int) broker.get("port"), (Map<String, String>) broker.get("resources"));
            this.withNode((String) node.get("host"), (int) node.get("port"));
            return this;

        }


    }
}
