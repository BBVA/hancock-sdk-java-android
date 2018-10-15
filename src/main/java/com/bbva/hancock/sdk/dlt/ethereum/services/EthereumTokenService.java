package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumSmartContractRetrieveResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransactionAdaptResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.smartContracts.EthereumContractInstance;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenInstance;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.allowance.EthereumTokenAllowanceRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.approve.EthereumTokenApproveRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.balance.EthereumTokenBalance;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.balance.EthereumTokenBalanceResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata.EthereumTokenMetadataResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata.EthereumTokenMetadata;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.register.EthereumTokenRegisterRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.register.EthereumTokenRegisterResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.transfer.EthereumTokenTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.transferFrom.EthereumTokenTransferFromRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.models.TransactionConfig;
import com.bbva.hancock.sdk.exception.HancockException;
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

    private HancockConfig config;

    private EthereumTransactionService transactionClient;

    public EthereumTokenService(HancockConfig config, EthereumTransactionService transactionClient) {
        this.config = config;
        this.transactionClient = transactionClient;
    }

    /**
     * Get the list of all tokens registered in Hancock
     * @return The list of all tokens registered in Hancock
     * @throws HancockException
     */
    public ArrayList<EthereumTokenInstance> getAllTokens() throws HancockException {

        String url = this.config.getAdapter().getHost() + ':' + this.config.getAdapter().getPort() + this.config.getAdapter().getBase() + this.config.getAdapter().getResources().get("tokenFindAll");

        Request request = getRequest(url);

        Response response = makeCall(request);
        EthereumSmartContractRetrieveResponse responseModel = checkStatus(response, EthereumSmartContractRetrieveResponse.class);
        return responseModel.getData();

    }

    /**
     * Get the token balance for account `address`
     * @param addressOrAlias Address or alias of the token smart contract registered in Hancock
     * @param address The token owner's address
     * @return The result of the request with the balance
     * @throws HancockException
     */
    public EthereumTokenBalance getBalance(String addressOrAlias, String address) throws HancockException {

        ValidateParameters.checkForContent(address, "address");
        ValidateParameters.checkAddress(address);
        ValidateParameters.checkForContent(addressOrAlias, "address or alias");
        String url = this.config.getAdapter().getHost() + ':' + this.config.getAdapter().getPort() + this.config.getAdapter().getBase() + this.config.getAdapter().getResources().get("tokenBalance").replaceAll("__ADDRESS__", address).replaceAll("__ADDRESS_OR_ALIAS__", addressOrAlias);

        Request request = getRequest(url);

        Response response = makeCall(request);
        EthereumTokenBalanceResponse responseModel = checkStatus(response, EthereumTokenBalanceResponse.class);
        return responseModel.getTokenBalance();

    }

    /**
     * Retrieves the metadata of the token
     * @param addressOrAlias Address or alias of the token smart contract registered in Hancock
     * @return name, symbol, decimals, and totalSupply of the token
     * @throws HancockException
     */
    public EthereumTokenMetadata getMetadata(String addressOrAlias) throws HancockException {

        ValidateParameters.checkForContent(addressOrAlias, "address or alias");
        String url = this.config.getAdapter().getHost() + ':' + this.config.getAdapter().getPort() + this.config.getAdapter().getBase() + this.config.getAdapter().getResources().get("tokenMetadata").replaceAll("__ADDRESS_OR_ALIAS__", addressOrAlias);

        Request request = getRequest(url);

        Response response = makeCall(request);
        EthereumTokenMetadataResponse responseModel = checkStatus(response, EthereumTokenMetadataResponse.class);
        return responseModel.getTokenMetadata();

    }

    /**
     * Register a new ERC20 token instance in Hancock
     * @param alias An alias for the token
     * @param address The address of the deployed smart contract token instance
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTokenRegisterResponse register(String alias, String address) throws Exception {

        ValidateParameters.checkForContent(address, "address");
        ValidateParameters.checkAddress(address);
        ValidateParameters.checkForContent(alias, "alias");
        String url = getResourceUrl(this.config,"tokenRegister");

        Gson gson = new Gson();
        EthereumTokenRegisterRequest hancockRequest = new EthereumTokenRegisterRequest(alias, address);
        String json = gson.toJson(hancockRequest);
        RequestBody body = RequestBody.create(this.CONTENT_TYPE_JSON, json);

        Request request = getRequest(url, body);

        Response response = makeCall(request);
        return checkStatus(response, EthereumTokenRegisterResponse.class);
    }

    /**
     * Transfer the balance from token owner's account to `to` account
     * - Owner's account must have sufficient balance to transfer
     * - 0 value transfers are allowed
     * @param request The data of the transaction (owner, receiver, amount of tokens (in weis) and the address/alias of the contract)
     * @param txConfig Configuration of how the transaction will be send to the network
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTransactionResponse transfer(EthereumTokenTransferRequest request, TransactionConfig txConfig) throws Exception{
        EthereumTransaction rawtx = this.adaptTransfer(request);
        return this.transactionClient.send(rawtx, txConfig);
    }

    /**
     * Transfer `tokens` from the `sender` account to the `to` account
     * The calling account must already have sufficient tokens approved for spending from the `sender` account and
     * - Sender account must have sufficient balance to transfer
     * - Spender must have sufficient allowance to transfer
     * - 0 value transfers are allowed
     * @param request The data of the transaction (approved spender's address, token sender's address, receiver, amount of tokens (in weis) and the address/alias of the contract)
     * @param txConfig Configuration of how the transaction will be send to the network
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTransactionResponse transferFrom(EthereumTokenTransferFromRequest request, TransactionConfig txConfig) throws Exception{
        EthereumTransaction rawtx = this.adaptTransfer(request);
        return this.transactionClient.send(rawtx, txConfig);
    }

    /**
     * Returns the amount of tokens approved by the owner that can be transferred to the spender's account
     * @param request The data of the transaction (caller's address, token owner's address, token spender's address, amount of tokens (in weis) and the address/alias of the contract)
     * @param txConfig Configuration of how the transaction will be send to the network
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTransactionResponse allowance(EthereumTokenAllowanceRequest request, TransactionConfig txConfig) throws Exception{
        EthereumTransaction rawtx = this.adaptTransfer(request);
        return this.transactionClient.send(rawtx, txConfig);
    }

    /**
     * Token owner can approve for `spender` to transferFrom(...) `tokens` from the token owner's account
     * @param request The data of the transaction (token owner's address, token spender's address, amount of tokens (in weis) and the address/alias of the contract)
     * @param txConfig Configuration of how the transaction will be send to the network
     * @return The result of the request
     * @throws Exception
     */
    public EthereumTransactionResponse approve(EthereumTokenApproveRequest request, TransactionConfig txConfig) throws Exception{
        EthereumTransaction rawtx = this.adaptTransfer(request);
        return this.transactionClient.send(rawtx, txConfig);
    }

    protected EthereumTransaction adaptTransfer(EthereumTokenRequest txRequest) throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(txRequest.getBody());
        RequestBody body = RequestBody.create(this.CONTENT_TYPE_JSON, json);
        String url = this.getTransferUrl(txRequest);
        Request request = getRequest(url, body);

        Response response = makeCall(request);
        EthereumTransactionAdaptResponse rawTx = checkStatus(response, EthereumTransactionAdaptResponse.class);
        return rawTx.getData();
    }

    protected String getTransferUrl(EthereumTokenRequest txRequest){
        String url;
        url = getResourceUrl(this.config,txRequest.getEncodeUrl()).replaceAll("__ADDRESS_OR_ALIAS__", txRequest.getAddressOrAlias());
        return url;
    }

}
