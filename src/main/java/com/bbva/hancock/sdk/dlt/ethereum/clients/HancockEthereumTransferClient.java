package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.HancockClient;
import com.bbva.hancock.sdk.HancockSocket;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.TransactionConfig;
import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.exception.HancockTypeErrorEnum;
import com.google.gson.Gson;

import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;

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

    /**
     * Send ethers between two accounts
     * @param tx Data of the transaction (sender address, receiver addres, amount of ether, data)
     * @param txConfig Configuration of how the transaction will be send to the network
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTransactionResponse send(EthereumTransferRequest tx, TransactionConfig txConfig) throws Exception{
        EthereumRawTransaction rawtx = this.adaptTransfer(tx);
        return this.transactionClient.send(rawtx, txConfig);
    }

    /**
     * Create a websocket subscription to watch transactions of type "transfer" in the network
     * @param addresses An array of address that will be added to the watch list
     * @param consumer A consumer plugin previously configured in hancock that will handle each received event
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribe(ArrayList<String> addresses, String consumer) throws HancockException {
        String url = getConfig().getBroker().getResources().get("events")
                .replaceAll("__ADDRESS__", "")
                .replaceAll("__SENDER__", "")
                .replaceAll("__CONSUMER__", consumer);
        try {
            HancockSocket socket = new HancockSocket(url);
            socket.addTransfer(addresses);
            return socket;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50003", 500, HancockErrorEnum.ERROR_SOCKET.getMessage() , HancockErrorEnum.ERROR_SOCKET.getMessage(), e);
        }
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
                rawTx.getFrom(),
                rawTx.getTo(),
                new BigInteger(rawTx.getNonce()),
                new BigInteger(rawTx.getValue()),
                rawTx.getData(),
                new BigInteger(rawTx.getGasPrice()),
                new BigInteger(rawTx.getGas())
            );
    }
}
