package com.bbva.hancock.sdk.dlt.ethereum.models.token;

import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumContractInstance;

public class EthereumTokenInstance extends EthereumContractInstance {

    private String name;
    private String symbol;
    private Integer decimals;
    private Integer totalSupply;

    public EthereumTokenInstance(String abiName, String alias, String address, String symbol, String name, Integer decimals, Integer totalSupply) {
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

    public Integer getTotalSupply() {
        return totalSupply;
    }
}
