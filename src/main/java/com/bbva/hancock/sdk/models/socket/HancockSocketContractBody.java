package com.bbva.hancock.sdk.models.socket;

import com.bbva.hancock.sdk.models.HancockCurrency;
import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class HancockSocketContractBody {

    private String blockHash;
    private Long blockNumber;
    private String transactionId;
    private String smartContractAddress;
    private String eventName;
    private HancockCurrency fee;
    private JsonObject returnValues;
    private Long timestamp;

}
