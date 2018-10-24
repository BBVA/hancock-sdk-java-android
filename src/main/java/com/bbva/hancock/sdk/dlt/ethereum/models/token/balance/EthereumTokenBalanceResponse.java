package com.bbva.hancock.sdk.dlt.ethereum.models.token.balance;

import com.bbva.hancock.sdk.models.HancockGenericResponse;

public class EthereumTokenBalanceResponse {
  private HancockGenericResponse result;
  public EthereumTokenBalance data;

  public EthereumTokenBalanceResponse(HancockGenericResponse result) {
    this.result = result;
  }
  
  public HancockGenericResponse getResult() {
      return result;
  }
  
  public EthereumTokenBalance getTokenBalance() {
      return this.data;
  }
}
