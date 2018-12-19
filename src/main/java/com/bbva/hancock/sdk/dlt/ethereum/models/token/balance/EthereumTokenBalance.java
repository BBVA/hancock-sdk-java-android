package com.bbva.hancock.sdk.dlt.ethereum.models.token.balance;

import java.io.Serializable;
import java.math.BigInteger;

public class EthereumTokenBalance implements Serializable {
    private static final long serialVersionUID = 7386823051261247154L;
    private BigInteger balance;
    private Integer accuracy;

    public EthereumTokenBalance() {
    }

    public BigInteger getBalance() {
        return balance;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setBalance(final BigInteger balance) {
        this.balance = balance;
    }

    public void setAccuracy(final Integer accuracy) {
        this.accuracy = accuracy;
    }
}
