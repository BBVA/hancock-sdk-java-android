package com.bbva.hancock.sdk.dlt.ethereum;

import org.web3j.crypto.RawTransaction;
import java.math.BigInteger;

public final class EthereumRawTransaction {

    private final RawTransaction rawTx;

    public EthereumRawTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value) {
        this(nonce, gasPrice, gasLimit, to, value, "");
    }

    public EthereumRawTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, String data) {
        this(nonce, gasPrice, gasLimit, to, BigInteger.ZERO, data);
    }

    public EthereumRawTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                          BigInteger value, String data) {

        data = data != null ? data : "";
        this.rawTx = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);

    }

    public BigInteger getNonce() {
        return this.rawTx.getNonce();
    }

    public BigInteger getGasPrice() {
        return this.rawTx.getGasPrice();
    }

    public BigInteger getGasLimit() {
        return this.rawTx.getGasLimit();
    }

    public String getTo() {
        return this.rawTx.getTo();
    }

    public BigInteger getValue() {
        return this.rawTx.getValue();
    }

    public String getData() {
        return this.rawTx.getData();
    }

    public RawTransaction getWeb3Instance() { return this.rawTx; }

}