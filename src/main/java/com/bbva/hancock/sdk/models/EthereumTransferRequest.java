package com.bbva.hancock.sdk.models;

public class EthereumTransferRequest {

    private String from;
    private String to;
    private String value;
    private String data;

    public EthereumTransferRequest(String from, String to, String value, String data) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.data = data;
    }

    public EthereumTransferRequest(String from, String to, String value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }

    public EthereumTransferRequest(String from) {
        this.from = from;
    }

    public EthereumTransferRequest(String from, String value) {
      this.from = from;
      this.value = value;
  }
    
    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getValue() {
        return value;
    }

    public String getData() {
        return data;
    }
}