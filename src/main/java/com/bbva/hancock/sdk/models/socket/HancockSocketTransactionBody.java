package com.bbva.hancock.sdk.models.socket;

import com.bbva.hancock.sdk.models.HancockCurrency;
import lombok.Data;

@Data
public class HancockSocketTransactionBody {

    private String blockHash;
    private Long blockNumber;
    private String transactionId;
    private String from;
    private String to;
    private HancockCurrency value;
    private String data;
    private HancockCurrency fee;
    private Long timeStamp;

}
