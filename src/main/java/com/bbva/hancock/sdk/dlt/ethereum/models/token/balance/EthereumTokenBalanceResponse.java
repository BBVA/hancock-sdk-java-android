package com.bbva.hancock.sdk.dlt.ethereum.models.token.balance;

import java.math.BigInteger;

public class EthereumTokenBalanceResponse {
    private  BigInteger balance;
    private  Integer decimals;

    public BigInteger getBalance() {
      return balance;
    }
    
    public Integer getDecimals() {
      return decimals;
    }
}
