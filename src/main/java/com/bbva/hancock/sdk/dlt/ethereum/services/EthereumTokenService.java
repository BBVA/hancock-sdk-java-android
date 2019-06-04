package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumSmartContractRetrieveResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransactionAdaptResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenInstance;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.allowance.EthereumTokenAllowanceRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.allowance.EthereumTokenAllowanceResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.approve.EthereumTokenApproveRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.balance.EthereumTokenBalance;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.balance.EthereumTokenBalanceResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata.EthereumTokenMetadata;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata.EthereumTokenMetadataResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.register.EthereumTokenRegisterRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.register.EthereumTokenRegisterResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.transfer.EthereumTokenTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.transferFrom.EthereumTokenTransferFromRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.models.TransactionConfig;
import com.bbva.hancock.sdk.util.ValidateParameters;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.ArrayList;

import static com.bbva.hancock.sdk.Common.*;

public class EthereumTokenService {

    private static final MediaType CONTENT_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private final HancockConfig config;

    private final EthereumTransactionService transactionClient;

    private final static Gson gson = new Gson();

    public EthereumTokenService(final HancockConfig config, final EthereumTransactionService transactionClient) {
        this.config = config;
        this.transactionClient = transactionClient;
    }

    /**
     * Get the list of all tokens registered in Hancock
     *
     * @return The list of all tokens registered in Hancock
     * @throws HancockException
     */
    public ArrayList<EthereumTokenInstance> getAllTokens() throws HancockException {

        final String url = config.getAdapter().getHost() + ':' + config.getAdapter().getPort() + config.getAdapter().getBase() + config.getAdapter().getResources().get("tokenFindAll");

        final Request request = getRequest(url);

        final Response response = makeCall(request);
        final EthereumSmartContractRetrieveResponse responseModel = checkStatus(response, EthereumSmartContractRetrieveResponse.class);
        return responseModel.getData();

    }

    /**
     * Get the token balance for account `address`
     *
     * @param addressOrAlias Address or alias of the token smart contract registered in Hancock
     * @param address        The token owner's address
     * @return The result of the request with the balance
     * @throws HancockException
     */
    public EthereumTokenBalance getBalance(final String addressOrAlias, final String address) throws HancockException {

        ValidateParameters.checkForContent(address, "address");
        ValidateParameters.checkAddress(address);
        ValidateParameters.checkForContent(addressOrAlias, "address or alias");
        final String url = config.getAdapter().getHost() + ':' + config.getAdapter().getPort() + config.getAdapter().getBase() + config.getAdapter().getResources().get("tokenBalance").replaceAll("__ADDRESS__", address).replaceAll("__ADDRESS_OR_ALIAS__", addressOrAlias);

        final Request request = getRequest(url);

        final Response response = makeCall(request);
        final EthereumTokenBalanceResponse responseModel = checkStatus(response, EthereumTokenBalanceResponse.class);
        return responseModel.getTokenBalance();

    }

    /**
     * Retrieves the metadata of the token
     *
     * @param addressOrAlias Address or alias of the token smart contract registered in Hancock
     * @return name, symbol, decimals, and totalSupply of the token
     * @throws HancockException
     */
    public EthereumTokenMetadata getMetadata(final String addressOrAlias) throws HancockException {

        ValidateParameters.checkForContent(addressOrAlias, "address or alias");
        final String url = config.getAdapter().getHost() + ':' + config.getAdapter().getPort() + config.getAdapter().getBase() + config.getAdapter().getResources().get("tokenMetadata").replaceAll("__ADDRESS_OR_ALIAS__", addressOrAlias);

        final Request request = getRequest(url);

        final Response response = makeCall(request);
        final EthereumTokenMetadataResponse responseModel = checkStatus(response, EthereumTokenMetadataResponse.class);
        return responseModel.getTokenMetadata();

    }

    /**
     * Register a new ERC20 token instance in Hancock
     *
     * @param alias   An alias for the token
     * @param address The address of the deployed smart contract token instance
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTokenRegisterResponse register(final String alias, final String address) throws Exception {

        ValidateParameters.checkForContent(address, "address");
        ValidateParameters.checkAddress(address);
        ValidateParameters.checkForContent(alias, "alias");
        final String url = getResourceUrl(config, "tokenRegister");


        final EthereumTokenRegisterRequest hancockRequest = new EthereumTokenRegisterRequest(alias, address);
        final String json = gson.toJson(hancockRequest);
        final RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);

        final Request request = getRequest(url, body);

        final Response response = makeCall(request);
        return checkStatus(response, EthereumTokenRegisterResponse.class);
    }

    /**
     * Transfer the balance from token owner's account to `to` account
     * - Owner's account must have sufficient balance to transfer
     * - 0 value transfers are allowed
     *
     * @param request  The data of the transaction (owner, receiver, amount of tokens (in weis) and the address/alias of the contract)
     * @param txConfig Configuration of how the transaction will be send to the network
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTransactionResponse transfer(final EthereumTokenTransferRequest request, final TransactionConfig txConfig) throws Exception {
        final EthereumTransaction rawtx = adaptTransfer(request);
        return transactionClient.send(rawtx, txConfig);
    }

    /**
     * Transfer `tokens` from the `sender` account to the `to` account
     * The calling account must already have sufficient tokens approved for spending from the `sender` account and
     * - Sender account must have sufficient balance to transfer
     * - Spender must have sufficient allowance to transfer
     * - 0 value transfers are allowed
     *
     * @param request  The data of the transaction (approved spender's address, token sender's address, receiver, amount of tokens (in weis) and the address/alias of the contract)
     * @param txConfig Configuration of how the transaction will be send to the network
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTransactionResponse transferFrom(final EthereumTokenTransferFromRequest request, final TransactionConfig txConfig) throws Exception {
        final EthereumTransaction rawtx = adaptTransfer(request);
        return transactionClient.send(rawtx, txConfig);
    }

    /**
     * Returns the amount of tokens approved by the owner that can be transferred to the spender's account
     *
     * @param request The data of the transaction (caller's address, token owner's address, token spender's address, amount of tokens (in weis) and the address/alias of the contract)
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTokenAllowanceResponse allowance(final EthereumTokenAllowanceRequest request) throws Exception {

        final String json = gson.toJson(request.getBody());
        final RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);
        final String url = getTransferUrl(request);
        final Request allowanceRequest = getRequest(url, body);

        final Response response = makeCall(allowanceRequest);
        return checkStatus(response, EthereumTokenAllowanceResponse.class);
    }

    /**
     * Token owner can approve for `spender` to transferFrom(...) `tokens` from the token owner's account
     *
     * @param request  The data of the transaction (token owner's address, token spender's address, amount of tokens (in weis) and the address/alias of the contract)
     * @param txConfig Configuration of how the transaction will be send to the network
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTransactionResponse approve(final EthereumTokenApproveRequest request, final TransactionConfig txConfig) throws Exception {
        final EthereumTransaction rawtx = adaptTransfer(request);
        return transactionClient.send(rawtx, txConfig);
    }

    protected EthereumTransaction adaptTransfer(final EthereumTokenRequest txRequest) throws Exception {

        final String json = gson.toJson(txRequest.getBody());
        final RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);
        final String url = getTransferUrl(txRequest);
        final Request request = getRequest(url, body);

        final Response response = makeCall(request);
        final EthereumTransactionAdaptResponse rawTx = checkStatus(response, EthereumTransactionAdaptResponse.class);
        return rawTx.getData();
    }

    protected String getTransferUrl(final EthereumTokenRequest txRequest) {
        final String url;
        url = getResourceUrl(config, txRequest.getEncodeUrl()).replaceAll("__ADDRESS_OR_ALIAS__", txRequest.getAddressOrAlias());
        return url;
    }

}
