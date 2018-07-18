package com.bbva.hancock.sdk.models;

import java.math.BigInteger;

public class TokenBalanceResponse {
    private  BigInteger balance;
    private  BigInteger accuracy;

    public BigInteger getBalance() {
      return balance;
    }
    
    public BigInteger getAccuracy() {
      return accuracy;
    }
}
