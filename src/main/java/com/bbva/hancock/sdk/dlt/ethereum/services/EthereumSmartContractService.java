package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.HancockSocket;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransactionAdaptResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.*;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumSignedTransactionRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.exception.HancockTypeErrorEnum;
import com.bbva.hancock.sdk.models.TransactionConfig;
import com.bbva.hancock.sdk.models.socket.HancockSocketTransactionBody;
import com.bbva.hancock.sdk.models.socket.HancockSocketTransactionEvent;
import com.bbva.hancock.sdk.util.ValidateParameters;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.core.methods.response.AbiDefinition;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import static com.bbva.hancock.sdk.Common.*;
import static com.bbva.hancock.sdk.models.socket.HancockSocketMessageResponseKind.SMARTCONTRACTDEPLOYMENT;

public class EthereumSmartContractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EthereumSmartContractService.class);
    private static final MediaType CONTENT_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private final EthereumTransactionService transactionClient;

    private final HancockConfig config;
    private final static Gson gson = new Gson();

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

        final EthereumTransactionAdaptResponse invokeResponse = adaptInvoke(contractAddressOrAlias, method, params, from);
        return transactionClient.send(invokeResponse.getData(), options);
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
        final Response response = adaptInvokeAbi(contractAddressOrAlias, method, params, from, action, abi);
        final EthereumTransactionAdaptResponse responseModel = checkStatus(response, EthereumTransactionAdaptResponse.class);

        return transactionClient.send(responseModel.getData(), options);
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

        final String url = generateUri(config.getAdapter().getHost(), config.getAdapter().getPort(), config.getAdapter().getBase(),
                config.getAdapter().getResources().get("invoke").replaceAll("__ADDRESS_OR_ALIAS__", contractAddressOrAlias));

        final EthereumAdaptInvokeRequest callBody = new EthereumAdaptInvokeRequest(method, from, params, "call");
        final Response response = createRequestAndMakeCall(url, callBody);
        return checkStatus(response, EthereumCallResponse.class);

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
        final Response response = adaptInvokeAbi(contractAddressOrAlias, method, params, from, action, abi);
        return checkStatus(response, EthereumCallResponse.class);

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

        final String url = generateUri(config.getAdapter().getHost(), config.getAdapter().getPort(), config.getAdapter().getBase(),
                config.getAdapter().getResources().get("register"));

        final EthereumRegisterRequest registerBody = new EthereumRegisterRequest(address, alias, abi);

        final Response response = createRequestAndMakeCall(url, registerBody);
        return checkStatus(response, EthereumRegisterResponse.class);

    }

    /**
     * Create a websocket subscription to watch transactions of type "contract-event" in the network
     *
     * @param contracts An array of address or alias that will be added to the watch list
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribeToEvents(final List<String> contracts) throws HancockException {
        return subscribeToEvents(contracts, "", null);
    }

    /**
     * Create a websocket subscription to watch transactions of type "contract-event" in the network
     *
     * @param contracts An array of address or alias that will be added to the watch list
     * @param consumer  A consumer plugin previously configured in hancock that will handle each received event
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribeToEvents(final List<String> contracts, final String consumer) throws HancockException {
        return subscribeToEvents(contracts, consumer, null);
    }

    /**
     * Create a websocket subscription to watch transactions of type "contract-event" in the network
     *
     * @param contracts An array of address or alias that will be added to the watch list
     * @param callback  A function to be called when the sockets has the connection ready. Has the socket as a param
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribeToEvents(final List<String> contracts, final Function callback) throws HancockException {
        return subscribeToEvents(contracts, "", callback);
    }

    /**
     * Create a websocket subscription to watch transactions of type "contract-event" in the network
     *
     * @param contracts An array of address or alias that will be added to the watch list
     * @param consumer  A consumer plugin previously configured in hancock that will handle each received event
     * @param callback  A function to be called when the sockets has the connection ready. Has the socket as a param
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribeToEvents(final List<String> contracts, final String consumer, final Function callback) throws HancockException {
        final String url = generateUri(config.getBroker().getHost(), config.getBroker().getPort(), config.getBroker().getBase(),
                config.getBroker().getResources().get("events")
                        .replaceAll("__ADDRESS__", "")
                        .replaceAll("__SENDER__", "")
                        .replaceAll("__CONSUMER__", consumer));

        try {
            final HancockSocket socket = new HancockSocket(url);
            socket.on("ready", o -> {
                socket.watchContractEvent(contracts);
                if (callback != null) {
                    callback.apply(socket);
                }
                return null;
            });
            return socket;
        } catch (final HancockException e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Create a websocket subscription to watch transactions of type "contract-transaction" in the network
     *
     * @param contracts An array of address or alias that will be added to the watch list
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribeToTransactions(final List<String> contracts) throws HancockException {
        return subscribeToTransactions(contracts, "", null);
    }

    /**
     * Create a websocket subscription to watch transactions of type "contract-transaction" in the network
     *
     * @param contracts An array of address or alias that will be added to the watch list
     * @param consumer  A consumer plugin previously configured in hancock that will handle each received event
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribeToTransactions(final List<String> contracts, final String consumer) throws HancockException {
        return subscribeToTransactions(contracts, consumer, null);
    }

    /**
     * Create a websocket subscription to watch transactions of type "contract-transaction" in the network
     *
     * @param contracts An array of address or alias that will be added to the watch list
     * @param callback  A function to be called when the sockets has the connection ready. Has the socket as a param
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribeToTransactions(final List<String> contracts, final Function callback) throws HancockException {
        return subscribeToTransactions(contracts, "", callback);
    }

    /**
     * Create a websocket subscription to watch transactions of type "contract-transaction" in the network
     *
     * @param contracts An array of address or alias that will be added to the watch list
     * @param consumer  A consumer plugin previously configured in hancock that will handle each received event
     * @param callback  A function to be called when the sockets has the connection ready. Has the socket as a param
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribeToTransactions(final List<String> contracts, final String consumer, final Function callback) throws HancockException {
        final String url = generateUri(config.getBroker().getHost(), config.getBroker().getPort(), config.getBroker().getBase(),
                config.getBroker().getResources().get("events")
                        .replaceAll("__ADDRESS__", "")
                        .replaceAll("__SENDER__", "")
                        .replaceAll("__CONSUMER__", consumer));

        try {
            final HancockSocket socket = new HancockSocket(url);
            socket.on("ready", o -> {
                socket.watchContractTransaction(contracts);
                if (callback != null) {
                    callback.apply(socket);
                }
                return null;
            });
            return socket;
        } catch (final HancockException e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Create a websocket subscription to watch transactions of type "contract-transaction" in the network
     *
     * @param contracts An array of address or alias that will be added to the watch list
     * @param consumer  A consumer plugin previously configured in hancock that will handle each received event
     * @param callback  A function to be called when the sockets has the connection ready. Has the socket as a param
     * @return A HancockSocket object which can add new subscriptions and listen incoming message
     * @throws HancockException
     */
    public HancockSocket subscribeToContractsDeployments(final List<String> contracts, final String consumer, final Function callback) throws HancockException {
        final String url = generateUri(config.getBroker().getHost(), config.getBroker().getPort(), config.getBroker().getBase(),
                config.getBroker().getResources().get("events")
                        .replaceAll("__ADDRESS__", "")
                        .replaceAll("__SENDER__", "")
                        .replaceAll("__CONSUMER__", consumer));

        try {
            final HancockSocket socket = new HancockSocket(url);
            socket.on("ready", o -> {
                socket.watchContractDeployments(contracts);
                if (callback != null) {
                    callback.apply(socket);
                }
                return null;
            });
            return socket;
        } catch (final HancockException e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    protected EthereumTransactionAdaptResponse adaptInvoke(final String contractAddressOrAlias, final String method, final ArrayList<String> params, final String from) throws HancockException {

        final String url = generateUri(config.getAdapter().getHost(), config.getAdapter().getPort(), config.getAdapter().getBase(),
                config.getAdapter().getResources().get("invoke").replaceAll("__ADDRESS_OR_ALIAS__", contractAddressOrAlias));

        final EthereumAdaptInvokeRequest invokebody = new EthereumAdaptInvokeRequest(method, from, params, "send");

        final Response response = createRequestAndMakeCall(url, invokebody);
        return checkStatus(response, EthereumTransactionAdaptResponse.class);

    }

    protected Response adaptInvokeAbi(final String to, final String method, final ArrayList<String> params, final String from, final String action, final ArrayList<AbiDefinition> abi) throws HancockException {

        final String url = generateUri(config.getAdapter().getHost(), config.getAdapter().getPort(), config.getAdapter().getBase(),
                config.getAdapter().getResources().get("invokeAbi"));

        final EthereumAdaptInvokeAbiRequest invokeabibody = new EthereumAdaptInvokeAbiRequest(method, from, params, action, to, abi);

        final String json = gson.toJson(invokeabibody);
        final RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);

        final Request request = getRequest(url, body);

        return makeCall(request);

    }

    private String generateUri(final String host, final int port, final String base, final String path) {
        final StringBuilder url = new StringBuilder(host);
        url.append(":");
        url.append(port);
        url.append(base);
        url.append(path);
        return url.toString();
    }


    private final Map<String, HancockSocketTransactionBody> deploySocketResponse = new HashMap<>();
    private final Map<String, String> keyTransactions = new HashMap<>();

    /**
     * Deploy a new smart contract instance
     *
     * @param from              The sender of the operation
     * @param urlBase           An url for the deploy
     * @param constructorName   An method to invoke
     * @param constructorParams The params of the call
     * @param options           The transaction config
     * @return The result of the request
     * @throws HancockException
     */
    public Future<HancockSocketTransactionBody> deploy(final String from, final String urlBase, final String constructorName, final List<String> constructorParams, final TransactionConfig options) throws Exception {

        final String key = String.valueOf(new Date().getTime());
        final ExecutorService executor = Executors.newSingleThreadExecutor();

        return executor.submit(() -> {
            //1. Open broker connection to receive messages
            final HancockSocket socket = subscribeToContractsDeployments(Collections.singletonList(from), "", msg -> checkDeployEvents((HancockSocket) msg, key));

            //2. Call to adapter deploy
            final String url = generateUri(config.getAdapter().getHost(), config.getAdapter().getPort(), config.getAdapter().getBase(),
                    config.getAdapter().getResources().get("deploy"));

            final EthereumDeployRequest deployBody = new EthereumDeployRequest(urlBase, constructorName, constructorParams, from);

            final Response response = createRequestAndMakeCall(url, deployBody);

            final EthereumTransactionAdaptResponse ethereumResponse = checkStatus(response, EthereumTransactionAdaptResponse.class);

            //3. Sign and send
            final EthereumDeploySendResponse deployResponse = send(ethereumResponse.getData(), options);
            keyTransactions.put(key, deployResponse.getTransactionHash());
            //4. Wait response en return
            do {
                Thread.sleep(300);
            } while (!deploySocketResponse.containsKey(deployResponse.getTransactionHash()));

            socket.removeAllListeners();
            socket.getWs().close();
            final HancockSocketTransactionBody hancockSocketTransactionBody = deploySocketResponse.get(deployResponse.getTransactionHash());
            deploySocketResponse.remove(deployResponse.getTransactionHash());
            return hancockSocketTransactionBody;
        });

    }

    private boolean checkDeployEvents(final HancockSocket socket, final String transactionHash) {
        socket.onTransaction(SMARTCONTRACTDEPLOYMENT, msg -> processEvent(msg, transactionHash));
        return true;
    }

    private Void processEvent(final HancockSocketTransactionEvent event, final String key) {
        final String transactionHash = keyTransactions.get(key);
        if (event.getBody().getTransactionId().equals(transactionHash)) {
            deploySocketResponse.put(transactionHash, event.getBody());
            keyTransactions.remove(key);
        }

        return null;
    }

    private EthereumDeploySendResponse send(final EthereumTransaction rawtx, final TransactionConfig txConfig) throws Exception {
        final String signedTransaction = transactionClient.signTransaction(new EthereumRawTransaction(rawtx), txConfig.getPrivateKey());

        final String url = config.getWallet().getHost() + ':' + config.getWallet().getPort() + config.getWallet().getBase() + config.getWallet().getResources().get("sendSignedTx");
        final EthereumSignedTransactionRequest signedTxBody = new EthereumSignedTransactionRequest(signedTransaction);

        final Response response = createRequestAndMakeCall(url, signedTxBody);
        final EthereumDeploySendResponse txResponse = checkStatus(response, EthereumDeploySendResponse.class);
        return txResponse;
    }

    private <T> Response createRequestAndMakeCall(final String url, final T bodyObj) throws HancockException {
        final String json = gson.toJson(bodyObj);
        final RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);
        final Request request = getRequest(url, body);
        return makeCall(request);
    }

}
