package com.bbva.hancock.sdk.models;

import java.math.BigInteger;

public class TokenBalanceResponse {
    private  BigInteger balance;
    private  Integer decimals;

    public BigInteger getBalance() {
      return balance;
    }
    
    public Integer getDecimals() {
      return decimals;
    }
}
