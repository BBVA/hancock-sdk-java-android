package com.bbva.hancock.sdk.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)
public class HancockConfigTest {

    @Test public void testConfig() {

        HancockConfig.Builder builder = new HancockConfig.Builder();
        HancockConfig config = builder.withAdapter("http://localhost", "/", 3000)
                .withBroker("http://localhost", "/", 3001)
                .withWallet("http://localhost", "/", 3002)
                .withEnv("dev")
                .withNode("http://localhost", 3003)
                .build();

        assertTrue("Config OK", config instanceof HancockConfig);
        assertEquals(config.getAdapter().getPort(), 3000);
        assertEquals(config.getBroker().getPort(), 3001);
        assertEquals(config.getWallet().getPort(), 3002);
        assertEquals(config.getAdapter().getHost(), "http://localhost");
        assertEquals(config.getBroker().getHost(), "http://localhost");
        assertEquals(config.getWallet().getHost(), "http://localhost");
        assertEquals(config.getEnv(), "dev");
        assertEquals(config.getNode().getHost(), "http://localhost");
        assertEquals(config.getNode().getPort(), 3003);
    }

    @Test public void testConfigWithoutParams() {

        HancockConfig.Builder builder = new HancockConfig.Builder();
        HancockConfig config = builder.build();

        assertTrue("Config OK", config instanceof HancockConfig);
        assertEquals(config.getAdapter().getPort(), 3000);
        assertEquals(config.getBroker().getPort(), 3000);
        assertEquals(config.getWallet().getPort(), 3000);
        assertEquals(config.getAdapter().getHost(), "http://localhost");
        assertEquals(config.getBroker().getHost(), "ws://localhost");
        assertEquals(config.getWallet().getHost(), "http://localhost");
        assertEquals(config.getEnv(), "local");
        assertEquals(config.getNode().getHost(), "http://localhost");
        assertEquals(config.getNode().getPort(), 8545);
    }
}
