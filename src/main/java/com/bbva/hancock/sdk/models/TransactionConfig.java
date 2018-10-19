package com.bbva.hancock.sdk.models;

public class TransactionConfig {

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
        return this.sendLocally;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void setSendLocally(boolean sendLocally) {
        this.sendLocally = sendLocally;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public HancockCallbackOptions getCallbackOptions() {
        return callbackOptions;
    }

    public void setCallbackOptions(HancockCallbackOptions callbackOptions) {
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

        public Builder withPrivateKey(String privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        public Builder withSendLocally(boolean sendLocally) {
            this.sendLocally = sendLocally;
            return this;
        }

        public Builder withNode(String node) {
            this.node = node;
            return this;
        }

        public Builder withProvider(String provider) {
            this.provider = provider;
            return this;
        }

        public Builder withCallbackOptions(String backUrl, String requestId) {
            this.callbackOptions = new HancockCallbackOptions(backUrl, requestId);
            return this;
        }

        public TransactionConfig build() {
            TransactionConfig config = new TransactionConfig();

            if (this.privateKey != null) {
                config.setPrivateKey(this.privateKey);
            } else if (this.provider != null) {
                config.setProvider(this.provider);
            }

            if (this.callbackOptions != null) {
                config.setCallbackOptions(this.callbackOptions);
            }

            if (this.sendLocally && this.node != null) {
                config.setNode(this.node);
            }

            config.setSendLocally(this.sendLocally);

            return config;
        }
    }

}
