package com.bbva.hancock.sdk.dlt.ethereum;

import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.services.*;
import com.bbva.hancock.sdk.services.ProtocolService;

public class EthereumClient {

    private ProtocolService protocolService;
    private EthereumWalletService walletService;
    private EthereumTransferService transferService;
    private EthereumTokenService tokenService;
    private EthereumSmartContractService smartContractService;
    private EthereumTransactionService transactionService;

    /**
     * Main interface to interact with Hancock's ethereum interface
     * @param config Configuration of Hancock (Adapter, Broker, WalletHub, DLT Node)
     * @throws Exception
     */
    public EthereumClient(HancockConfig config) {

        this.protocolService = new ProtocolService(config);
        this.walletService = new EthereumWalletService(config);
        this.transactionService = new EthereumTransactionService(config);
        this.transferService = new EthereumTransferService(config, this.transactionService);
        this.tokenService = new EthereumTokenService(config, this.transactionService);
        this.smartContractService = new EthereumSmartContractService(config, this.transactionService);

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