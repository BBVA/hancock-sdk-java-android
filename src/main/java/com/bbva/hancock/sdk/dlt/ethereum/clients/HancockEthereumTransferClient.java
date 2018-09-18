package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.HancockClient;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.TransactionConfig;
import com.google.gson.Gson;

import java.math.BigInteger;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bbva.hancock.sdk.Common.checkStatus;
import static com.bbva.hancock.sdk.Common.getRequest;
import static com.bbva.hancock.sdk.Common.getResourceUrl;
import static com.bbva.hancock.sdk.Common.makeCall;

public class HancockEthereumTransferClient extends HancockClient {

    private HancockEthereumTransactionClient transactionClient;

    public HancockEthereumTransferClient(HancockEthereumTransactionClient transactionClient) {
        super();
        this.transactionClient = transactionClient;
    }

    public HancockEthereumTransferClient(HancockConfig config, HancockEthereumTransactionClient transactionClient) throws Exception {
        super(config);
        this.transactionClient = transactionClient;
    }

    public EthereumTransactionResponse send(EthereumTransferRequest request, TransactionConfig txConfig) throws Exception{
        EthereumRawTransaction rawtx = this.adaptTransfer(request);
        return this.transactionClient.send(rawtx, txConfig);
    }

    protected EthereumRawTransaction adaptTransfer(EthereumTransferRequest txRequest) throws Exception {
        String url = getResourceUrl(getConfig(),"transfer");
        Gson gson = new Gson();
        String json = gson.toJson(txRequest);
        RequestBody body = RequestBody.create(getContentType(), json);
        Request request = getRequest(url, body);

        Response response = makeCall(request);
        EthereumTransaction rawTx = checkStatus(response, EthereumTransaction.class);
        return new EthereumRawTransaction(
                new BigInteger(rawTx.getNonce()),
                new BigInteger(rawTx.getGasPrice()),
                new BigInteger(rawTx.getGas()),
                rawTx.getTo(),
                new BigInteger(rawTx.getValue()),
                rawTx.getData()
            );
    }
}
