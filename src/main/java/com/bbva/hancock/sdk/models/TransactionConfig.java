package com.bbva.hancock.sdk.models;

public class TransactionConfig {

    private String privateKey;
    private String provider;
    private boolean locally;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isLocally() {
        return locally;
    }

    public void setLocally(boolean locally) {
        this.locally = locally;
    }
}
