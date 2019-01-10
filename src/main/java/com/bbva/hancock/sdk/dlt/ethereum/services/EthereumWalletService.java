package com.bbva.hancock.sdk.dlt.ethereum.services;

import com.bbva.hancock.sdk.Common;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.EthereumWallet;
import com.bbva.hancock.sdk.dlt.ethereum.models.wallet.GetBalanceResponse;
import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.exception.HancockTypeErrorEnum;
import com.bbva.hancock.sdk.util.ValidateParameters;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.math.BigInteger;

import static com.bbva.hancock.sdk.Common.checkStatus;

public class EthereumWalletService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EthereumWalletService.class);
    private final HancockConfig config;

    public EthereumWalletService(final HancockConfig config) {
        this.config = config;
    }

    /**
     * Generates a new wallet
     *
     * @return address, publicKey, and privateKey of the new wallet
     * @throws HancockException
     */
    public EthereumWallet generateWallet() throws HancockException {

        try {
            final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            final Credentials credentials = Credentials.create(ecKeyPair);

            final String address = credentials.getAddress();
            final String privateKey = "0x" + credentials.getEcKeyPair().getPrivateKey().toString(16);
            final String publicKey = "0x" + credentials.getEcKeyPair().getPublicKey().toString(16);

            return new EthereumWallet(address, privateKey, publicKey);

        } catch (final Exception error) {
            LOGGER.error("Wallet error: ", error.toString());
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50003", 500, HancockErrorEnum.ERROR_WALLET.getMessage(), HancockErrorEnum.ERROR_WALLET.getMessage(), error);
        }

    }

    /**
     * Retrieves the ethers balance of an account
     *
     * @param address The token owner's address
     * @return The account balance (in weis)
     * @throws HancockException
     */
    public BigInteger getBalance(final String address) throws HancockException {

        ValidateParameters.checkForContent(address, "address");
        ValidateParameters.checkAddress(address);
        final String url = this.config.getAdapter().getHost() + ':' + this.config.getAdapter().getPort() + this.config.getAdapter().getBase() + this.config.getAdapter().getResources().get("balance").replaceAll("__ADDRESS__", address);

        final Request request = Common.getRequest(url);

        final Response response = Common.makeCall(request);
        final GetBalanceResponse responseModel = checkStatus(response, GetBalanceResponse.class);
        return new BigInteger(responseModel.getBalance());

    }

}
