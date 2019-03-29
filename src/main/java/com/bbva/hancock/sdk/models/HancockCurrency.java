package com.bbva.hancock.sdk.models;

import lombok.Data;

@Data
public class HancockCurrency {

    private String amount;
    private Integer decimals;
    private String currency;
}
