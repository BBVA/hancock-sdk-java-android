package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.HancockClient;
import com.bbva.hancock.sdk.HancockSocket;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.HancockGenericResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumAdaptInvokeRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumAdaptInvokeResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumCallResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumRegisterRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.TransactionConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.util.ValidateParameters;
import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.exception.HancockTypeErrorEnum;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bbva.hancock.sdk.Common.checkStatus;
import static com.bbva.hancock.sdk.Common.getRequest;
import static com.bbva.hancock.sdk.Common.makeCall;

public class HancockEthereumSmartContractClient extends HancockClient {

    private HancockEthereumTransactionClient transactionClient;

    public HancockEthereumSmartContractClient(){
        super();
    }

    public HancockEthereumSmartContractClient(HancockConfig config, HancockEthereumTransactionClient transactionClient) throws Exception {
        super(config);
        this.transactionClient = transactionClient;
    }

    /**
     * Makes an invocation to an smart contract method.
     * Invocations are used to call smart contract methods that writes information in the blockchain consuming gas
     * @param contractAddressOrAlias Address or alias of the smart contract registered in Hancock
     * @param method The name of the method to call
     * @param params An array of arguments passed to the method
     * @param from The address of the account doing the call
     * @param options Configuration of how the transaction will be send to the network
     * @return The returned value from the smart contract method
     * @throws Exception
     */
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
                invokeResponse.getData().getFrom(),
                invokeResponse.getData().getTo(),
                new BigInteger(invokeResponse.getData().getNonce()),
                new BigInteger(invokeResponse.getData().getValue()),
                invokeResponse.getData().getData(),
                new BigInteger(invokeResponse.getData().getGasPrice()),
                new BigInteger(invokeResponse.getData().getGas())
        );
        return this.transactionClient.send(rawTx, options);
    }

    /**
     * Makes a call to an smart contract method. Calls only fetch information from blockchain so it doesn't consume gas
     * @param contractAddressOrAlias Address or alias of the smart contract registered in Hancock
     * @param method The name of the method to call
     * @param params An array of arguments passed to the method
     * @param from The address of the account doing the call
     * @return The returned value from the smart contract method
     * @throws HancockException
     */
    public EthereumCallResponse call(String contractAddressOrAlias, String method, ArrayList<String> params, String from) throws HancockException {

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
        EthereumCallResponse responseModel = checkStatus(response, EthereumCallResponse.class);
        return responseModel;
    }

    //TODO register: how to pass the abi??

    /**
     * Register a new smart contract instance in Hancock
     * @param alias An alias for the smart contract
     * @param address The address of the deployed smart contract instance
     * @param abi The application binary interface (abi) of the deployed smart contract
     * @return The result of the request
     * @throws HancockException
     */
    public HancockGenericResponse register(String alias, String address, ArrayList<Object> abi) throws HancockException {

        ValidateParameters.checkForContent(alias, "Alias");
        ValidateParameters.checkForContent(address, "Address");
        ValidateParameters.checkAddress(address);

        String url = getConfig().getAdapter().getHost() + ':'
                + getConfig().getAdapter().getPort()
                + getConfig().getAdapter().getBase()
                + getConfig().getAdapter().getResources().get("register");

        EthereumRegisterRequest registerBody = new EthereumRegisterRequest(alias, address, abi);

        Gson gson = new Gson();
        String json = gson.toJson(registerBody);
        RequestBody body = RequestBody.create(getContentType(), json);

        Request request = getRequest(url, body);

        Response response = makeCall(request);
        HancockGenericResponse responseModel = checkStatus(response, HancockGenericResponse.class);
        return responseModel;
    }

    /**
     * Create a websocket subscription to watch transactions of type "contracts" in the network
     * @param contracts An array of address or alias that will be added to the watch list
     * @param consumer A consumer plugin previously configured in hancock that will handle each received event
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribe(ArrayList<String> contracts, String consumer) throws HancockException{
        String url = getConfig().getBroker().getResources().get("events")
                .replaceAll("__ADDRESS__", "")
                .replaceAll("__SENDER__", "")
                .replaceAll("__CONSUMER__", consumer);
        try {
            HancockSocket socket = new HancockSocket(url);
            socket.addContract(contracts);
            return socket;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50003", 500, HancockErrorEnum.ERROR_SOCKET.getMessage() , HancockErrorEnum.ERROR_SOCKET.getMessage(), e);
        }
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
