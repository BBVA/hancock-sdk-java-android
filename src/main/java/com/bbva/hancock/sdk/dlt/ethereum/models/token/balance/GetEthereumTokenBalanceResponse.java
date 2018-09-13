package com.bbva.hancock.sdk.dlt.ethereum.models.token.balance;

class GetEthereumTokenBalanceResponseResult {
  public int code;
  public String description;
}

class GetEthereumTokenBalanceResponseData {
  public EthereumTokenBalanceResponse tokenbalance;
}


public class GetEthereumTokenBalanceResponse {
  public GetEthereumTokenBalanceResponseResult result;
  public GetEthereumTokenBalanceResponseData data;

  public EthereumTokenBalanceResponse getTokenBalance() {
      return this.data.tokenbalance;
  }
}
