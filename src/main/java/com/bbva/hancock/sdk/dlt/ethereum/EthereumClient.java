package com.bbva.hancock.sdk.dlt.ethereum;

import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.services.*;
import com.bbva.hancock.sdk.services.ProtocolService;

public class EthereumClient {

    private final ProtocolService protocolService;
    private final EthereumWalletService walletService;
    private final EthereumTransferService transferService;
    private final EthereumTokenService tokenService;
    private final EthereumSmartContractService smartContractService;
    private final EthereumTransactionService transactionService;

    /**
     * Main interface to interact with Hancock's ethereum interface
     *
     * @param config Configuration of Hancock (Adapter, Broker, WalletHub, DLT Node)
     */
    public EthereumClient(final HancockConfig config) {

        protocolService = new ProtocolService(config);
        walletService = new EthereumWalletService(config);
        transactionService = new EthereumTransactionService(config);
        transferService = new EthereumTransferService(config, transactionService);
        tokenService = new EthereumTokenService(config, transactionService);
        smartContractService = new EthereumSmartContractService(config, transactionService);

    }

    public ProtocolService getProtocolService() {
        return protocolService;
    }

    public EthereumWalletService getWalletService() {
        return walletService;
    }

    public EthereumTransferService getTransferService() {
        return transferService;
    }

    public EthereumTokenService getTokenService() {
        return tokenService;
    }

    public EthereumSmartContractService getSmartContractService() {
        return smartContractService;
    }

    public EthereumTransactionService getTransactionService() {
        return transactionService;
    }

}
