package com.bbva.hancock.sdk.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

public class HancockConfig implements Serializable {

    private static final long serialVersionUID = -1366311853326659203L;
    private String env;
    public HancockConfigNode node;
    public HancockConfigService adapter;
    public HancockConfigService wallet;
    public HancockConfigService broker;

    public HancockConfig() {
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(final String env) {
        this.env = env;
    }

    public HancockConfigNode getNode() {
        return node;
    }

    public void setNode(final HancockConfigNode node) {
        this.node = node;
    }

    public HancockConfigService getAdapter() {
        return adapter;
    }

    public void setAdapter(final HancockConfigService adapter) {
        this.adapter = adapter;
    }

    public HancockConfigService getWallet() {
        return wallet;
    }

    public void setWallet(final HancockConfigService wallet) {
        this.wallet = wallet;
    }

    public HancockConfigService getBroker() {
        return broker;
    }

    public void setBroker(final HancockConfigService broker) {
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
            fromConfigFile();
        }

        /**
         * Configuration of environment
         *
         * @param env Environment name
         * @return Builder
         */
        public Builder withEnv(final String env) {
            this.env = env;
            return this;
        }

        /**
         * Configuration of DLT node to connect to
         *
         * @param host Host of the node
         * @param port Port of the node
         * @return Builder
         */
        public Builder withNode(final String host, final int port) {

            if (node == null) {
                node = new HancockConfigNode();
            }

            node.setHost(host);
            node.setPort(port);

            return this;
        }

        /**
         * Configuration of Hancock's Adapter
         *
         * @param host Host of Adapter
         * @param base Base String to build url endpoints of Adapter
         * @param port Port of Adapter
         * @return Builder
         */
        public Builder withAdapter(final String host, final String base, final int port) {

            if (adapter == null) {
                adapter = new HancockConfigService();
            }

            adapter.setHost(host);
            adapter.setBase(base);
            adapter.setPort(port);

            return this;
        }

        /**
         * Configuration of Hancock's Wallet
         *
         * @param host Host of Wallet
         * @param base Base String to build url endpoints of Wallet
         * @param port Port of Wallet
         * @return Builder
         */
        public Builder withWallet(final String host, final String base, final int port) {

            if (wallet == null) {
                wallet = new HancockConfigService();
            }

            wallet.setHost(host);
            wallet.setBase(base);
            wallet.setPort(port);

            return this;
        }

        /**
         * Configuration of Hancock's Broker
         *
         * @param host Host of Broker
         * @param base Base String to build url endpoints of Broker
         * @param port Port of Broker
         * @return Builder
         */
        public Builder withBroker(final String host, final String base, final int port) {

            if (broker == null) {
                broker = new HancockConfigService();
            }

            broker.setHost(host);
            broker.setBase(base);
            broker.setPort(port);

            return this;
        }

        public HancockConfig build() {

            final HancockConfig config = new HancockConfig();

            config.setEnv(env);

            if (node != null) {
                config.setNode(node);
            }

            if (adapter != null) {
                config.setAdapter(adapter);
            }

            if (wallet != null) {
                config.setWallet(wallet);
            }

            if (broker != null) {
                config.setBroker(broker);
            }

            return config;
        }


        protected Builder withAdapter(final String host, final String base, final int port, final Map<String, String> resources) {
            adapter = new HancockConfigService(host, base, port, resources);
            return this;
        }

        protected Builder withWallet(final String host, final String base, final int port, final Map<String, String> resources) {
            wallet = new HancockConfigService(host, base, port, resources);
            return this;
        }

        protected Builder withBroker(final String host, final String base, final int port, final Map<String, String> resources) {
            broker = new HancockConfigService(host, base, port, resources);
            return this;
        }

        protected Builder fromConfigFile() {

            final InputStream input = getClass().getClassLoader().getResourceAsStream("application.yml");
            final Yaml yaml = new Yaml();

            final Map<String, Object> object = (Map<String, Object>) yaml.load(input);

            final String env = (String) object.get("env");
            final Map<String, Object> node = (Map<String, Object>) object.get("node");
            final Map<String, Object> adapter = (Map<String, Object>) object.get("adapter");
            final Map<String, Object> wallet = (Map<String, Object>) object.get("wallet");
            final Map<String, Object> broker = (Map<String, Object>) object.get("broker");

            withEnv(env);
            withAdapter((String) adapter.get("host"), (String) adapter.get("base"), (int) adapter.get("port"), (Map<String, String>) adapter.get("resources"));
            withWallet((String) wallet.get("host"), (String) wallet.get("base"), (int) wallet.get("port"), (Map<String, String>) wallet.get("resources"));
            withBroker((String) broker.get("host"), (String) broker.get("base"), (int) broker.get("port"), (Map<String, String>) broker.get("resources"));
            withNode((String) node.get("host"), (int) node.get("port"));
            return this;

        }

    }
}
