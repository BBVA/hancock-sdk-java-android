package com.bbva.hancock.sdk.models.socket;

import lombok.Getter;

public enum HancockSocketMessageResponseKind {
    TRANSFER ("transfer"),
    TRANSACTION ("transaction"),
    SMARTCONTRACTTRANSACTION ("contract-transaction"),
    SMARTCONTRACTEVENT ("contract-event");

    @Getter
    private final String kind;

    HancockSocketMessageResponseKind(String kind){
        this.kind = kind;
    }

}
