package com.bbva.hancock.sdk.dlt.ethereum;

public class EthereumWallet implements java.io.Serializable {

    private String address;
    private String privateKey;
    private String publicKey;

    public EthereumWallet() {
    }

    public EthereumWallet(String address, String privateKey, String publicKey) {
        this.address = address;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public EthereumWallet(EthereumWallet copy) {
        this.address = copy.getAddress();
        this.privateKey = copy.getPrivateKey();
        this.publicKey = copy.getPublicKey();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(String publicKey) {
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