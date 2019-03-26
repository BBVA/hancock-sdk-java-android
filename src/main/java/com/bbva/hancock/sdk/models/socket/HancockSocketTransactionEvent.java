package com.bbva.hancock.sdk.models.socket;

import lombok.Data;

@Data
public class HancockSocketTransactionEvent extends HancockSocketMessage {

    private HancockSocketTransactionBody body;


}
