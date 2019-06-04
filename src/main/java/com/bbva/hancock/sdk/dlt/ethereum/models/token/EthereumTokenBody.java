package com.bbva.hancock.sdk.dlt.ethereum.models.token;

import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumBody;

import java.io.Serializable;

public class EthereumTokenBody extends EthereumBody implements Serializable {

    private static final long serialVersionUID = 8190822402396330128L;

    private String spender;
    private String tokenOwner;
    private String sender;

    public EthereumTokenBody() {

    }

    public EthereumTokenBody(final String from) {
        super(from);
    }

    public EthereumTokenBody(final String from, final String value) {
        super(from, value);
    }

    public EthereumTokenBody(final String from, final String to, final String value) {
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

    public void setSpender(final String spender) {
        this.spender = spender;
    }

    public void setTokenOwner(final String tokenOwner) {
        this.tokenOwner = tokenOwner;
    }

    public void setSender(final String sender) {
        this.sender = sender;
    }

}
