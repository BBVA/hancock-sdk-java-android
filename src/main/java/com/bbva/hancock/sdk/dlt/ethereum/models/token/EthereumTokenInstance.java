package com.bbva.hancock.sdk.dlt.ethereum.models.token;

import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumContractInstance;

import java.io.Serializable;
import java.math.BigInteger;

public class EthereumTokenInstance extends EthereumContractInstance implements Serializable {

    private static final long serialVersionUID = 15681663557180477L;
    private String name;
    private String symbol;
    private Integer decimals;
    private BigInteger totalSupply;

    public EthereumTokenInstance() {
    }

    public EthereumTokenInstance(final String abiName, final String alias, final String address, final String symbol, final String name, final Integer decimals, final BigInteger totalSupply) {
        super(abiName, alias, address);
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
        this.totalSupply = totalSupply;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public BigInteger getTotalSupply() {
        return totalSupply;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public void setTotalSupply(BigInteger totalSupply) {
        this.totalSupply = totalSupply;
    }
}
