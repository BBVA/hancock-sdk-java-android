package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.HancockClient;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumRawTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumTransaction;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.EthereumTokenRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.allowance.EthereumTokenAllowanceRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.approve.EthereumTokenApproveRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.balance.EthereumTokenBalanceResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.balance.GetEthereumTokenBalanceResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata.GetEthereumTokenMetadataResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.metadata.GetEthereumTokenMetadataResponseData;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.register.EthereumTokenRegisterRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.register.EthereumTokenRegisterResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.transfer.EthereumTokenTransferRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.token.transferFrom.EthereumTokenTransferFromRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.EthereumTransactionResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.transaction.TransactionConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.util.ValidateParameters;
import com.bbva.hancock.sdk.exception.HancockException;
import com.google.gson.Gson;

import java.math.BigInteger;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bbva.hancock.sdk.Common.checkStatus;
import static com.bbva.hancock.sdk.Common.getRequest;
import static com.bbva.hancock.sdk.Common.getResourceUrl;
import static com.bbva.hancock.sdk.Common.makeCall;

public class HancockEthereumTokenClient extends HancockClient {

    private HancockEthereumTransactionClient transactionClient;

    public HancockEthereumTokenClient(HancockEthereumTransactionClient transactionClient) {
        super();
        this.transactionClient = transactionClient;
    }

    public HancockEthereumTokenClient(HancockConfig config, HancockEthereumTransactionClient transactionClient) throws Exception {
        super(config);
        this.transactionClient = transactionClient;
    }

    /**
     * Get the token balance for account `address`
     * @param addressOrAlias Address or alias of the token smart contract registered in Hancock
     * @param address The token owner's address
     * @return The result of the request with the balance
     * @throws HancockException
     */
    public EthereumTokenBalanceResponse getBalance(String addressOrAlias, String address) throws HancockException {

        ValidateParameters.checkForContent(address, "address");
        ValidateParameters.checkAddress(address);
        ValidateParameters.checkForContent(addressOrAlias, "address or alias");
        String url = getConfig().getAdapter().getHost() + ':' + getConfig().getAdapter().getPort() + getConfig().getAdapter().getBase() + getConfig().getAdapter().getResources().get("tokenBalance").replaceAll("__ADDRESS__", address).replaceAll("__ADDRESS_OR_ALIAS__", addressOrAlias);

        Request request = getRequest(url);

        Response response = makeCall(request);
        GetEthereumTokenBalanceResponse responseModel = checkStatus(response, GetEthereumTokenBalanceResponse.class);
        return responseModel.getTokenBalance();

    }

    /**
     * Retrieves the metadata of the token
     * @param addressOrAlias Address or alias of the token smart contract registered in Hancock
     * @return name, symbol, decimals, and totalSupply of the token
     * @throws HancockException
     */
    public GetEthereumTokenMetadataResponseData getMetadata(String addressOrAlias) throws HancockException {

        ValidateParameters.checkForContent(addressOrAlias, "address or alias");
        String url = getConfig().getAdapter().getHost() + ':' + getConfig().getAdapter().getPort() + getConfig().getAdapter().getBase() + getConfig().getAdapter().getResources().get("tokenMetadata").replaceAll("__ADDRESS_OR_ALIAS__", addressOrAlias);

        Request request = getRequest(url);

        Response response = makeCall(request);
        GetEthereumTokenMetadataResponse responseModel = checkStatus(response, GetEthereumTokenMetadataResponse.class);
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
        String url = getResourceUrl(getConfig(),"tokenRegister");

        Gson gson = new Gson();
        EthereumTokenRegisterRequest hancockRequest = new EthereumTokenRegisterRequest(alias, address);
        String json = gson.toJson(hancockRequest);
        RequestBody body = RequestBody.create(getContentType(), json);

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
        EthereumRawTransaction rawtx = this.adaptTransfer(request);
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
        EthereumRawTransaction rawtx = this.adaptTransfer(request);
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
        EthereumRawTransaction rawtx = this.adaptTransfer(request);
        return this.transactionClient.send(rawtx, txConfig);
    }

    /**
     * Token owner can approve for `spender` to transferFrom(...) `tokens` from the token owner's account
     * @param request The data of the transaction (token owner's address, token spender's address, amount of tokens (in weis) and the address/alias of the contract)
     * @param txConfig Configuration of how the transaction will be send to the network
     * @returnThe result of the request
     * @throws Exception
     */
    public EthereumTransactionResponse approve(EthereumTokenApproveRequest request, TransactionConfig txConfig) throws Exception{
        EthereumRawTransaction rawtx = this.adaptTransfer(request);
        return this.transactionClient.send(rawtx, txConfig);
    }

    protected EthereumRawTransaction adaptTransfer(EthereumTokenRequest txRequest) throws Exception {
        String url = getTransferUrl(txRequest);
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

    protected String getTransferUrl(EthereumTokenRequest txRequest){
        String url;
        url = getResourceUrl(getConfig(),txRequest.getEncodeUrl()).replaceAll("__ADDRESS_OR_ALIAS__", txRequest.getAddressOrAlias());
        return url;
    }

}
