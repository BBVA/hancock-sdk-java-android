package com.bbva.hancock.sdk.models;

class GetTokenBalanceResponseResult {
  public int code;
  public String description;
}

class GetTokenBalanceResponseData {
  public TokenBalanceResponse tokenbalance;
}


public class GetTokenBalanceResponse {
  public GetTokenBalanceResponseResult result;
  public GetTokenBalanceResponseData data;

  public TokenBalanceResponse getTokenBalance() {
      return this.data.tokenbalance;
  }
}
