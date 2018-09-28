package com.bbva.hancock.sdk.dlt.ethereum.models.token.register;

class EthereumTokenRegisterResponseResult {
    public int code;
    public String description;
}

public class EthereumTokenRegisterResponse {
    private EthereumTokenRegisterResponseResult result;

    public EthereumTokenRegisterResponseResult getResult() {
        return result;
    }

}
