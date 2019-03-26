package com.bbva.hancock.sdk.models.socket;

import lombok.Getter;

public enum HancockSocketMessageRequestKind {
    WATCHTRANSFER ("watch-transfers"),
    WATCHTRANSACTION ("watch-transactions"),
    WATCHSMARTCONTRACT ("watch-contracts"),
    WATCHSMARTCONTRACTTRANSACTION ("watch-contract-transactions"),
    WATCHSMARTCONTRACTEVENT ("watch-contracts-events"),
    UNWATCHTRANSFER ("unwatch-transfers"),
    UNWATCHTRANSACTION ("unwatch-transactions"),
    UNWATCHSMARTCONTRACT ("unwatch-contracts"),
    UNWATCHSMARTCONTRACTTRANSACTION ("unwatch-contract-transactions"),
    UNWATCHSMARTCONTRACTEVENT ("unwatch-contracts-events");

    @Getter
    private final String kind;

    HancockSocketMessageRequestKind(String kind){
        this.kind = kind;
    }

}
