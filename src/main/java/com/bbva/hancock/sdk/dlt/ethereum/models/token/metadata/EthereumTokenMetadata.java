package com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata;

import java.io.Serializable;

public class EthereumTokenMetadata implements Serializable {
    private static final long serialVersionUID = -5132213981532037715L;
    public String name;
    public String symbol;
    public Integer decimals;
    public Integer totalSupply;

    public EthereumTokenMetadata() {
    }

    public Integer getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(final Integer totalSupply) {
        this.totalSupply = totalSupply;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(final Integer decimals) {
        this.decimals = decimals;
    }

}
