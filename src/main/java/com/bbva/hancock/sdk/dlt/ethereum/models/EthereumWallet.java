package com.bbva.hancock.sdk.dlt.ethereum.models;

import java.io.Serializable;

public class EthereumWallet implements Serializable {

    private static final long serialVersionUID = -3028994169964742049L;
    private String address;
    private String privateKey;
    private String publicKey;

    public EthereumWallet() {
    }

    public EthereumWallet(final String address, final String privateKey, final String publicKey) {
        this.address = address;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public EthereumWallet(final EthereumWallet copy) {
        this.address = copy.getAddress();
        this.privateKey = copy.getPrivateKey();
        this.publicKey = copy.getPublicKey();
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public void setPrivateKey(final String privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(final String publicKey) {
        this.publicKey = publicKey;
    }

    public String getAddress() {
        return (this.address);
    }

    public String getPrivateKey() {
        return (this.privateKey);
    }

    public String getPublicKey() {
        return (this.publicKey);
    }


}
