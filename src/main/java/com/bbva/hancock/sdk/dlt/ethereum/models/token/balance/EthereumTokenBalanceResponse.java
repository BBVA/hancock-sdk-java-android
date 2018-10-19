package com.bbva.hancock.sdk.dlt.ethereum.models.token.balance;

class EthereumTokenBalanceResponseResult {
  public int code;
  public String description;
}


public class EthereumTokenBalanceResponse {
  public EthereumTokenBalanceResponseResult result;
  public EthereumTokenBalance data;

  public EthereumTokenBalance getTokenBalance() {
      return this.data;
  }
}
