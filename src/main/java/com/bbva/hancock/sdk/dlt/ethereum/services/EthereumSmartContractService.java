package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.HancockSocket;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransactionAdaptResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.*;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.exception.HancockTypeErrorEnum;
import com.bbva.hancock.sdk.models.TransactionConfig;
import com.bbva.hancock.sdk.util.ValidateParameters;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.core.methods.response.AbiDefinition;

import java.net.URISyntaxException;
import java.util.ArrayList;

import static com.bbva.hancock.sdk.Common.*;

public class EthereumSmartContractService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EthereumSmartContractService.class);
    private static final MediaType CONTENT_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private final EthereumTransactionService transactionClient;

    private final HancockConfig config;

    public EthereumSmartContractService(final HancockConfig config, final EthereumTransactionService transactionClient) {
        this.config = config;
        this.transactionClient = transactionClient;
    }

    /**
     * Makes an invocation to an smart contract method.
     * Invocations are used to call smart contract methods that writes information in the blockchain consuming gas
     *
     * @param contractAddressOrAlias Address or alias of the smart contract registered in Hancock
     * @param method                 The name of the method to call
     * @param params                 An array of arguments passed to the method
     * @param from                   The address of the account doing the call
     * @param options                Configuration of how the transaction will be send to the network
     * @return The returned value from the smart contract method
     * @throws Exception
     */
    public EthereumTransactionResponse invoke(final String contractAddressOrAlias, final String method, final ArrayList<String> params, final String from, final TransactionConfig options) throws Exception {

        if (options.getPrivateKey() == null && options.getProvider() == null) {
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50007", 500, HancockErrorEnum.ERROR_NOKEY_NOPROVIDER.getMessage(), HancockErrorEnum.ERROR_NOKEY_NOPROVIDER.getMessage());
        }
        ValidateParameters.checkForContent(contractAddressOrAlias, "Address or Alias");
        ValidateParameters.checkForContent(method, "Method");
        ValidateParameters.checkForContent(from, "From");
        ValidateParameters.checkAddress(from);

        final EthereumTransactionAdaptResponse invokeResponse = this.adaptInvoke(contractAddressOrAlias, method, params, from);
        return this.transactionClient.send(invokeResponse.getData(), options);
    }

    /**
     * Makes an invocation to an smart contract method with a specific raw abi.
     * Invocations are used to call smart contract methods that writes information in the blockchain consuming gas
     *
     * @param contractAddressOrAlias Address or alias of the smart contract registered in Hancock
     * @param method                 The name of the method to call
     * @param params                 An array of arguments passed to the method
     * @param from                   The address of the account doing the call
     * @param options                Configuration of how the transaction will be send to the network
     * @param abi                    raw in json format
     * @return The returned value from the smart contract method
     * @throws Exception
     */
    public EthereumTransactionResponse invokeAbi(final String contractAddressOrAlias, final String method, final ArrayList<String> params, final String from, final TransactionConfig options, final ArrayList<AbiDefinition> abi) throws Exception {

        if (options.getPrivateKey() == null && options.getProvider() == null) {
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50007", 500, HancockErrorEnum.ERROR_NOKEY_NOPROVIDER.getMessage(), HancockErrorEnum.ERROR_NOKEY_NOPROVIDER.getMessage());
        }
        ValidateParameters.checkForContent(contractAddressOrAlias, "Address or Alias");
        ValidateParameters.checkForContent(method, "Method");
        ValidateParameters.checkForContent(from, "From");
        ValidateParameters.checkAddress(from);

        final String action = "send";
        final Response response = this.adaptInvokeAbi(contractAddressOrAlias, method, params, from, action, abi);
        final EthereumTransactionAdaptResponse responseModel = checkStatus(response, EthereumTransactionAdaptResponse.class);
        return this.transactionClient.send(responseModel.getData(), options);
    }

    /**
     * Makes a call to an smart contract method. Calls only fetch information from blockchain so it doesn't consume gas
     *
     * @param contractAddressOrAlias Address or alias of the smart contract registered in Hancock
     * @param method                 The name of the method to call
     * @param params                 An array of arguments passed to the method
     * @param from                   The address of the account doing the call
     * @return The returned value from the smart contract method
     * @throws HancockException
     */
    public EthereumCallResponse call(final String contractAddressOrAlias, final String method, final ArrayList<String> params, final String from) throws HancockException {

        ValidateParameters.checkForContent(contractAddressOrAlias, "Address or Alias");
        ValidateParameters.checkForContent(method, "Method");
        ValidateParameters.checkForContent(from, "From");
        ValidateParameters.checkAddress(from);

        final String url = this.config.getAdapter().getHost() + ':'
                + this.config.getAdapter().getPort()
                + this.config.getAdapter().getBase()
                + this.config.getAdapter().getResources().get("invoke").replaceAll("__ADDRESS_OR_ALIAS__", contractAddressOrAlias);

        final EthereumAdaptInvokeRequest callBody = new EthereumAdaptInvokeRequest(method, from, params, "call");
        final Gson gson = new Gson();
        final String json = gson.toJson(callBody);
        final RequestBody body = RequestBody.create(this.CONTENT_TYPE_JSON, json);

        final Request request = getRequest(url, body);

        final Response response = makeCall(request);
        final EthereumCallResponse responseModel = checkStatus(response, EthereumCallResponse.class);
        return responseModel;
    }

    /**
     * Makes a call to an smart contract method. Calls only fetch information from blockchain so it doesn't consume gas
     *
     * @param contractAddressOrAlias Address or alias of the smart contract registered in Hancock
     * @param method                 The name of the method to call
     * @param params                 An array of arguments passed to the method
     * @param from                   The address of the account doing the call
     * @param abi                    raw in json format
     * @return The returned value from the smart contract method
     * @throws HancockException
     */
    public EthereumCallResponse callAbi(final String contractAddressOrAlias, final String method, final ArrayList<String> params, final String from, final ArrayList<AbiDefinition> abi) throws HancockException {

        ValidateParameters.checkForContent(contractAddressOrAlias, "Address or Alias");
        ValidateParameters.checkForContent(method, "Method");
        ValidateParameters.checkForContent(from, "From");
        ValidateParameters.checkAddress(from);

        final String action = "call";
        final Response response = this.adaptInvokeAbi(contractAddressOrAlias, method, params, from, action, abi);
        final EthereumCallResponse responseModel = checkStatus(response, EthereumCallResponse.class);
        return responseModel;
    }

    //TODO register: how to pass the abi??

    /**
     * Register a new smart contract instance in Hancock
     *
     * @param alias   An alias for the smart contract
     * @param address The address of the deployed smart contract instance
     * @param abi     The application binary interface (abi) of the deployed smart contract
     * @return The result of the request
     * @throws HancockException
     */
    public EthereumRegisterResponse register(final String alias, final String address, final ArrayList<AbiDefinition> abi) throws HancockException {

        ValidateParameters.checkForContent(alias, "Alias");
        ValidateParameters.checkForContent(address, "Address");
        ValidateParameters.checkAddress(address);

        final String url = this.config.getAdapter().getHost() + ':'
                + this.config.getAdapter().getPort()
                + this.config.getAdapter().getBase()
                + this.config.getAdapter().getResources().get("register");

        final EthereumRegisterRequest registerBody = new EthereumRegisterRequest(address, alias, abi);

        final Gson gson = new Gson();
        final String json = gson.toJson(registerBody);
        final RequestBody body = RequestBody.create(this.CONTENT_TYPE_JSON, json);

        final Request request = getRequest(url, body);

        final Response response = makeCall(request);
        final EthereumRegisterResponse responseModel = checkStatus(response, EthereumRegisterResponse.class);
        return responseModel;
    }

    /**
     * Create a websocket subscription to watch transactions of type "contracts" in the network
     *
     * @param contracts An array of address or alias that will be added to the watch list
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribe(final ArrayList<String> contracts) throws HancockException {
        return this.subscribe(contracts, "");
    }

    /**
     * Create a websocket subscription to watch transactions of type "contracts" in the network
     *
     * @param contracts An array of address or alias that will be added to the watch list
     * @param consumer  A consumer plugin previously configured in hancock that will handle each received event
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribe(final ArrayList<String> contracts, final String consumer) throws HancockException {
        final String url = this.config.getBroker().getHost() + ':'
                + this.config.getBroker().getPort()
                + this.config.getBroker().getBase()
                + this.config.getBroker().getResources().get("events")
                .replaceAll("__ADDRESS__", "")
                .replaceAll("__SENDER__", "")
                .replaceAll("__CONSUMER__", consumer);
        try {
            final HancockSocket socket = new HancockSocket(url);
            socket.on("ready", o -> {
                socket.addTransfer(contracts);
                return null;
            });
            socket.addContract(contracts);
            return socket;
        } catch (final URISyntaxException e) {
            LOGGER.error(e.getMessage());
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50003", 500, HancockErrorEnum.ERROR_SOCKET.getMessage(), HancockErrorEnum.ERROR_SOCKET.getMessage(), e);
        }
    }

    protected EthereumTransactionAdaptResponse adaptInvoke(final String contractAddressOrAlias, final String method, final ArrayList<String> params, final String from) throws HancockException {

        final String url = this.config.getAdapter().getHost() + ':'
                + this.config.getAdapter().getPort()
                + this.config.getAdapter().getBase()
                + this.config.getAdapter().getResources().get("invoke").replaceAll("__ADDRESS_OR_ALIAS__", contractAddressOrAlias);

        final EthereumAdaptInvokeRequest invokebody = new EthereumAdaptInvokeRequest(method, from, params, "send");

        final Gson gson = new Gson();
        final String json = gson.toJson(invokebody);
        final RequestBody body = RequestBody.create(this.CONTENT_TYPE_JSON, json);

        final Request request = getRequest(url, body);

        final Response response = makeCall(request);
        final EthereumTransactionAdaptResponse responseModel = checkStatus(response, EthereumTransactionAdaptResponse.class);
        return responseModel;
    }

    protected Response adaptInvokeAbi(final String to, final String method, final ArrayList<String> params, final String from, final String action, final ArrayList<AbiDefinition> abi) throws HancockException {

        final String url = this.config.getAdapter().getHost() + ':'
                + this.config.getAdapter().getPort()
                + this.config.getAdapter().getBase()
                + this.config.getAdapter().getResources().get("invokeAbi");

        final EthereumAdaptInvokeAbiRequest invokeabibody = new EthereumAdaptInvokeAbiRequest(method, from, params, action, to, abi);

        final Gson gson = new Gson();
        final String json = gson.toJson(invokeabibody);
        final RequestBody body = RequestBody.create(this.CONTENT_TYPE_JSON, json);

        final Request request = getRequest(url, body);

        final Response response = makeCall(request);
        return response;
    }
}
