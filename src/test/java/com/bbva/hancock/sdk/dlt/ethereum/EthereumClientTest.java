package com.bbva.hancock.sdk.dlt.ethereum;

import com.bbva.hancock.sdk.config.HancockConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EthereumClientTest {


    @Test
    public void testCreateEthereumClient() {
        final HancockConfig.Builder builder = new HancockConfig.Builder();
        final HancockConfig config = builder.withAdapter("http://localhost", "/", 3000)
                .withBroker("http://localhost", "/", 3001)
                .withWallet("http://localhost", "/", 3002)
                .withEnv("dev")
                .withNode("http://localhost", 3003)
                .build();
        final EthereumClient client = new EthereumClient(config);

        Assert.assertNotNull(client.getSmartContractService());
        Assert.assertNotNull(client.getProtocolService());
        Assert.assertNotNull(client.getTokenService());
        Assert.assertNotNull(client.getTransactionService());
        Assert.assertNotNull(client.getTransferService());
    }

}
