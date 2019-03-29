package com.bbva.hancock.sdk.models.socket;

import lombok.Data;

@Data
public class HancockSocketContractEvent extends HancockSocketMessage {

    private HancockSocketContractBody body;
}
