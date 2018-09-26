package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.HancockClient;
import com.bbva.hancock.sdk.config.HancockConfig;

public class EthereumClient extends HancockClient {

    private EthereumProtocolClient protocolClient;
    private EthereumWalletClient walletClient;
    private EthereumTransferClient transferClient;
    private EthereumTokenClient tokenClient;
    private EthereumSmartContractClient smartContractClient;
    private EthereumTransactionClient transactionClient;

    /**
     * Main interface to interact with Hancock's ethereum interface
     * @param config Configuration of Hancock (Adapter, Broker, WalletHub, DLT Node)
     * @throws Exception
     */
    public EthereumClient(HancockConfig config) throws Exception {

        super(config);
        this.protocolClient = new EthereumProtocolClient(config);
        this.walletClient = new EthereumWalletClient(config);
        this.transactionClient = new EthereumTransactionClient(config);
        this.transferClient = new EthereumTransferClient(config, this.transactionClient);
        this.tokenClient = new EthereumTokenClient(config, this.transactionClient);
        this.smartContractClient = new EthereumSmartContractClient(config, this.transactionClient);
    }

    public EthereumProtocolClient getProtocolClient() {
        return protocolClient;
    }

    public EthereumWalletClient getWalletClient() {
        return walletClient;
    }

    public EthereumTransferClient getTransferClient() {
        return transferClient;
    }

    public EthereumTokenClient getTokenClient() {
        return tokenClient;
    }

    public EthereumSmartContractClient getSmartContractClient() {
        return smartContractClient;
    }

    public EthereumTransactionClient getTransactionClient() {
        return transactionClient;
    }

}