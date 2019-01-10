package com.bbva.hancock.sdk.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)
public class HancockConfigTest {

    @Test
    public void testConfig() {

        final HancockConfig.Builder builder = new HancockConfig.Builder();
        final HancockConfig config = builder.withAdapter("http://localhost", "/", 3000)
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

    @PrepareForTest({HancockConfig.Builder.class})
    @Test
    public void testConfigWithoutParams() throws Exception {

        final HancockConfig.Builder builder = new HancockConfig.Builder();
        mockStatic(HancockConfig.Builder.class);
        PowerMockito.doReturn(mock(HancockConfig.Builder.class)).when(mock(HancockConfig.Builder.class)).fromConfigFile();
        final HancockConfig config = builder.build();

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
