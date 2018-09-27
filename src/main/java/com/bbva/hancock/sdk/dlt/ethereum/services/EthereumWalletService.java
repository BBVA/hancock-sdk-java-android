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
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.math.BigInteger;

import static com.bbva.hancock.sdk.Common.checkStatus;

public class EthereumWalletService {

    private HancockConfig config;

    public EthereumWalletService(HancockConfig config) {
        this.config = config;
    }

    /**
     * Generates a new wallet
     * @return address, publicKey, and privateKey of the new wallet
     * @throws HancockException
     */
    public EthereumWallet generateWallet() throws HancockException {

        try {

            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            Credentials credentials = Credentials.create(ecKeyPair);

            String address = credentials.getAddress();
            String privateKey = "0x" + credentials.getEcKeyPair().getPrivateKey().toString(16);
            String publicKey = "0x" + credentials.getEcKeyPair().getPublicKey().toString(16);

            return new EthereumWallet(address, privateKey, publicKey);

        }catch (Exception error) {

            System.out.println("Wallet error: " + error.toString());
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50003", 500, HancockErrorEnum.ERROR_WALLET.getMessage() , HancockErrorEnum.ERROR_WALLET.getMessage(), error);

        }

    }

    /**
     * Retrieves the ethers balance of an account
     * @param address The token owner's address
     * @return The account balance (in weis)
     * @throws HancockException
     */
    public BigInteger getBalance(String address) throws HancockException {

        ValidateParameters.checkForContent(address, "address");
        ValidateParameters.checkAddress(address);
        String url = this.config.getAdapter().getHost() + ':' + this.config.getAdapter().getPort() + this.config.getAdapter().getBase() + this.config.getAdapter().getResources().get("balance").replaceAll("__ADDRESS__", address);

        Request request = Common.getRequest(url);

        Response response = Common.makeCall(request);
        GetBalanceResponse responseModel = checkStatus(response, GetBalanceResponse.class);
        return new BigInteger(responseModel.getBalance());

    }

}
