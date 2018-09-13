package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.HancockClient;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.EthereumWallet;
import com.bbva.hancock.sdk.dlt.ethereum.models.wallet.GetBalanceResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.util.ValidateParameters;
import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.exception.HancockTypeErrorEnum;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.math.BigInteger;

import okhttp3.Request;
import okhttp3.Response;

import static com.bbva.hancock.sdk.Common.*;

public class HancockEthereumWalletClient extends HancockClient {

    public HancockEthereumWalletClient() {
    }

    public HancockEthereumWalletClient(HancockConfig config) throws Exception {
        super(config);
    }

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

    public BigInteger getBalance(String address) throws HancockException {

        ValidateParameters.checkForContent(address, "address");
        ValidateParameters.checkAddress(address);
        String url = getConfig().getAdapter().getHost() + ':' + getConfig().getAdapter().getPort() + getConfig().getAdapter().getBase() + getConfig().getAdapter().getResources().get("balance").replaceAll("__ADDRESS__", address);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = makeCall(request);
        GetBalanceResponse responseModel = checkStatus(response, GetBalanceResponse.class);
        return new BigInteger(responseModel.getBalance());

    }

}
