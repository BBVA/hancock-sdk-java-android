package com.bbva.hancock.sdk.models.socket;

import lombok.Getter;

public enum HancockSocketMessageRequestKind {
    WATCHTRANSFER("watch-transfers"),
    WATCHTRANSACTION("watch-transactions"),
    WATCHSMARTCONTRACT("watch-contracts"),
    WATCHSMARTCONTRACTTRANSACTION("watch-contracts-transactions"),
    WATCHSMARTCONTRACTEVENT("watch-contracts-events"),
    UNWATCHTRANSFER("unwatch-transfers"),
    UNWATCHTRANSACTION("unwatch-transactions"),
    UNWATCHSMARTCONTRACT("unwatch-contracts"),
    UNWATCHSMARTCONTRACTTRANSACTION("unwatch-contracts-transactions"),
    UNWATCHSMARTCONTRACTEVENT("unwatch-contracts-events");

    @Getter
    private final String kind;

    HancockSocketMessageRequestKind(final String kind) {
        this.kind = kind;
    }

}
