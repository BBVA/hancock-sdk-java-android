package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.HancockClient;
import com.bbva.hancock.sdk.config.HancockConfig;

public class HancockEthereumClient extends HancockClient {

    private HancockEthereumProtocolClient protocolClient;
    private HancockEthereumWalletClient walletClient;
    private HancockEthereumTransferClient transferClient;
    private HancockEthereumTokenClient tokenClient;
    private HancockEthereumSmartContractClient smartContractClient;
    private HancockEthereumTransactionClient transactionClient;

    /**
     * Main interface to interact with Hancock's ethereum interface
     * @param config
     * @throws Exception
     */
    public HancockEthereumClient(HancockConfig config) throws Exception {

        super(config);
        this.protocolClient = new HancockEthereumProtocolClient(config);
        this.walletClient = new HancockEthereumWalletClient(config);
        this.transactionClient = new HancockEthereumTransactionClient(config);
        this.transferClient = new HancockEthereumTransferClient(config, this.transactionClient);
        this.tokenClient = new HancockEthereumTokenClient(config, this.transactionClient);
        this.smartContractClient = new HancockEthereumSmartContractClient(config, this.transactionClient);
    }

    public HancockEthereumProtocolClient getProtocolClient() {
        return protocolClient;
    }

    public HancockEthereumWalletClient getWalletClient() {
        return walletClient;
    }

    public HancockEthereumTransferClient getTransferClient() {
        return transferClient;
    }

    public HancockEthereumTokenClient getTokenClient() {
        return tokenClient;
    }

    public HancockEthereumSmartContractClient getSmartContractClient() {
        return smartContractClient;
    }

    public HancockEthereumTransactionClient getTransactionClient() {
        return transactionClient;
    }

}