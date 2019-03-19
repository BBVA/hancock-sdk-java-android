package com.bbva.hancock.sdk.dlt.ethereum.models.token.allowance;

import com.bbva.hancock.sdk.dlt.ethereum.models.token.balance.EthereumTokenBalance;
import com.bbva.hancock.sdk.models.HancockGenericResponse;

import java.io.Serializable;
import java.math.BigInteger;

public class EthereumTokenAllowanceResponse implements Serializable {

    private static final long serialVersionUID = -8862825278018978169L;
    private HancockGenericResponse result;
    public BigInteger data;

    public EthereumTokenAllowanceResponse() {

    }

    public EthereumTokenAllowanceResponse(final HancockGenericResponse result) {
        this.result = result;
    }

    public HancockGenericResponse getResult() {
        return result;
    }

    public BigInteger getData() {
        return data;
    }

    public void setData(final BigInteger data) {
        this.data = data;
    }

}
