package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.HancockClient;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumAdaptInvokeRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumAdaptInvokeResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.balance.GetEthereumTokenBalanceResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.register.EthereumTokenRegisterRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.TransactionConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.util.ValidateParameters;
import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.exception.HancockTypeErrorEnum;
import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.ArrayList;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bbva.hancock.sdk.Common.checkStatus;
import static com.bbva.hancock.sdk.Common.getRequest;
import static com.bbva.hancock.sdk.Common.makeCall;

public class HancockEthereumSmartContractClient extends HancockClient {

    private HancockEthereumTransactionClient transactionClient;

    public HancockEthereumSmartContractClient(HancockConfig config, HancockEthereumTransactionClient transactionClient) throws Exception {
        super(config);
        this.transactionClient = transactionClient;
    }

    public EthereumTransactionResponse invoke(String contractAddressOrAlias, String method, ArrayList<String> params, String from, TransactionConfig options) throws Exception {

        if (options.getPrivateKey() == null && options.getProvider() == null) {
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50007", 500, HancockErrorEnum.ERROR_NOKEY_NOPROVIDER.getMessage(), HancockErrorEnum.ERROR_NOKEY_NOPROVIDER.getMessage());
        }
        ValidateParameters.checkForContent(contractAddressOrAlias, "Address or Alias");
        ValidateParameters.checkForContent(method, "Method");
        ValidateParameters.checkForContent(from, "From");
        ValidateParameters.checkAddress(from);

        EthereumAdaptInvokeResponse invokeResponse = this.adaptInvoke(contractAddressOrAlias, method, params, from);
        EthereumRawTransaction rawTx = new EthereumRawTransaction(
                new BigInteger(invokeResponse.getData().getNonce()),
                new BigInteger(invokeResponse.getData().getGasPrice()),
                new BigInteger(invokeResponse.getData().getGas()),
                invokeResponse.getData().getTo(),
                new BigInteger(invokeResponse.getData().getValue()),
                invokeResponse.getData().getData()
        );
        return this.transactionClient.send(rawTx, options);
    }

    public EthereumAdaptInvokeResponse call(String contractAddressOrAlias, String method, ArrayList<String> params, String from) throws HancockException {

        ValidateParameters.checkForContent(contractAddressOrAlias, "Address or Alias");
        ValidateParameters.checkForContent(method, "Method");
        ValidateParameters.checkForContent(from, "From");
        ValidateParameters.checkAddress(from);

        String url = getConfig().getAdapter().getHost() + ':'
                + getConfig().getAdapter().getPort()
                + getConfig().getAdapter().getBase()
                + getConfig().getAdapter().getResources().get("invoke").replaceAll("__ADDRESS_OR_ALIAS__", contractAddressOrAlias);

        EthereumAdaptInvokeRequest callBody = new EthereumAdaptInvokeRequest(method, from, params, "call");

        Gson gson = new Gson();
        String json = gson.toJson(callBody);
        RequestBody body = RequestBody.create(getContentType(), json);

        Request request = getRequest(url, body);

        Response response = makeCall(request);
        EthereumAdaptInvokeResponse responseModel = checkStatus(response, EthereumAdaptInvokeResponse.class);
        return responseModel;
    }

    protected EthereumAdaptInvokeResponse adaptInvoke(String contractAddressOrAlias, String method, ArrayList<String> params, String from) throws HancockException {

        String url = getConfig().getAdapter().getHost() + ':'
                + getConfig().getAdapter().getPort()
                + getConfig().getAdapter().getBase()
                + getConfig().getAdapter().getResources().get("invoke").replaceAll("__ADDRESS_OR_ALIAS__", contractAddressOrAlias);

        EthereumAdaptInvokeRequest invokebody = new EthereumAdaptInvokeRequest(method, from, params, "send");

        Gson gson = new Gson();
        String json = gson.toJson(invokebody);
        RequestBody body = RequestBody.create(getContentType(), json);

        Request request = getRequest(url, body);

        Response response = makeCall(request);
        EthereumAdaptInvokeResponse responseModel = checkStatus(response, EthereumAdaptInvokeResponse.class);
        return responseModel;
    }
}
