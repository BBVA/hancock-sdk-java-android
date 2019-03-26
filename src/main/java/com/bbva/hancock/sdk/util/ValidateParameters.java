package com.bbva.hancock.sdk.util;

import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.exception.HancockTypeErrorEnum;

public final class ValidateParameters {

    private static final String addressPattern = "^(0x)?([a-fA-F0-9]{40})";
    private static final String message = " is empty";

    public static void checkForContent(final String param, final String var) throws HancockException {
        if (param == null || param.isEmpty()) {
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50005", 500, HancockErrorEnum.ERROR_PARAMETER.getMessage(), var + message);
        }
    }

    public static void checkAddress(final String address) throws HancockException {
        if (!address.matches(addressPattern)) {
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50006", 500, HancockErrorEnum.ERROR_FORMAT.getMessage(), HancockErrorEnum.ERROR_FORMAT.getMessage());
        }
    }

    public static String normalizeAlias(final String alias) {
        return alias.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();
    }

    public static String normalizeAddress(String address) {
        address = address.toLowerCase();
        if (address.indexOf("0x") != 0) {
            return "0x" + address;
        }
        return address;
    }

    public static String normalizeAdressOrAlias(final String addressOrAlias) {
        if (addressOrAlias.matches(addressPattern)) {
            return normalizeAddress(addressOrAlias);
        }
        return normalizeAlias(addressOrAlias);
    }

    // PRIVATE
    private ValidateParameters() {
        //empty - prevent construction
    }

}
