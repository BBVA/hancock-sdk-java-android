package com.bbva.hancock.sdk.dlt.ethereum.models.token.balance;

import java.math.BigInteger;

public class EthereumTokenBalance {
    private  BigInteger balance;
    private  Integer accuracy;

    public BigInteger getBalance() {
      return balance;
    }
    
    public Integer getAccuracy() {
      return accuracy;
    }
}
