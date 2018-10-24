package com.bbva.hancock.sdk.dlt.ethereum.models.token.register;

import com.bbva.hancock.sdk.models.HancockGenericResponse;

public class EthereumTokenRegisterResponse {
    private HancockGenericResponse result;

    public EthereumTokenRegisterResponse(HancockGenericResponse result) {
      this.result = result;
  }
    
    public HancockGenericResponse getResult() {
        return result;
    }

}
