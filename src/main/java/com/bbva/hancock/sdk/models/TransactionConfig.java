package com.bbva.hancock.sdk.models;

import java.io.Serializable;

public class TransactionConfig implements Serializable {

    private static final long serialVersionUID = 841616785347312981L;
    private String privateKey;
    private String provider;
    private HancockCallbackOptions callbackOptions;
    private String node;
    private boolean sendLocally = true;

    public String getNode() {
        return node;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getProvider() {
        return provider;
    }

    public boolean getSendLocally() {
        return sendLocally;
    }

    public void setNode(final String node) {
        this.node = node;
    }

    public void setSendLocally(final boolean sendLocally) {
        this.sendLocally = sendLocally;
    }

    public void setPrivateKey(final String privateKey) {
        this.privateKey = privateKey;
    }

    public void setProvider(final String provider) {
        this.provider = provider;
    }

    public HancockCallbackOptions getCallbackOptions() {
        return callbackOptions;
    }

    public void setCallbackOptions(final HancockCallbackOptions callbackOptions) {
        this.callbackOptions = callbackOptions;
    }

    public boolean isSendLocally() {
        return sendLocally;
    }

    public TransactionConfig() {
    }

    public static class Builder {

        private String privateKey;
        private String provider;
        private String node;
        private HancockCallbackOptions callbackOptions;
        private boolean sendLocally = false;

        public Builder withPrivateKey(final String privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        public Builder withSendLocally(final boolean sendLocally) {
            this.sendLocally = sendLocally;
            return this;
        }

        public Builder withNode(final String node) {
            this.node = node;
            return this;
        }

        public Builder withProvider(final String provider) {
            this.provider = provider;
            return this;
        }

        public Builder withCallbackOptions(final String backUrl, final String requestId) {
            callbackOptions = new HancockCallbackOptions(backUrl, requestId);
            return this;
        }

        public TransactionConfig build() {
            final TransactionConfig config = new TransactionConfig();

            if (privateKey != null) {
                config.setPrivateKey(privateKey);
            } else if (provider != null) {
                config.setProvider(provider);
            }

            if (callbackOptions != null) {
                config.setCallbackOptions(callbackOptions);
            }

            if (sendLocally && node != null) {
                config.setNode(node);
            }

            config.setSendLocally(sendLocally);

            return config;
        }
    }
}
