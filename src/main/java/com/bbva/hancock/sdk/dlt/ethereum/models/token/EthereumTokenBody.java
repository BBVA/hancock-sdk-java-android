package com.bbva.hancock.sdk.dlt.ethereum.models.token;

import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumBody;

public class EthereumTokenBody extends EthereumBody{

    private String spender;
    private String tokenOwner;
    private String sender;

    public EthereumTokenBody(String from) {
        super(from);
    }

    public EthereumTokenBody(String from, String value) {
        super(from, value);
    }

    public EthereumTokenBody(String from, String to, String value) {
        super(from, to, value);
    }

    public String getSpender() {
        return spender;
    }

    public String getTokenOwner() {
        return tokenOwner;
    }

    public String getSender() {
        return sender;
    }

    public void setSpender(String spender) {
        this.spender = spender;
    }

    public void setTokenOwner(String tokenOwner) {
        this.tokenOwner = tokenOwner;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
