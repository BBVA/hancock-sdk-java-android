package com.bbva.hancock.sdk;

import com.bbva.hancock.sdk.models.socket.HancockSocketContractEvent;
import com.bbva.hancock.sdk.models.socket.HancockSocketMessageRequestKind;
import com.bbva.hancock.sdk.models.socket.HancockSocketTransactionEvent;
import org.java_websocket.client.WebSocketClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WebSocketClient.class})
public class HancockSocketTest {

    private ArrayList<String> address;
    private ArrayList<String> contracts;
    private HancockSocket socketTest;
    private HancockSocket socket_spy;

    @Before
    public void setUp() throws URISyntaxException {

        contracts = new ArrayList<>();
        address = new ArrayList<>();
        socketTest = new HancockSocket("http://localhost:3000");
        socket_spy = spy(socketTest);


        PowerMockito.doNothing().when(socket_spy).sendMessage(any(String.class), any(ArrayList.class));

    }

    @Test
    public void testHancockSocket() throws Exception {

        assertEquals(new URI("http://localhost:3000"), socketTest.getWs().getURI());
        assertEquals(0, socketTest.getCallbackFunctions().size());
        assertTrue("HancockSocket create correctly", socketTest instanceof HancockSocket);
    }

    @Test
    public void testHancockSocketOn() {

        final Function func = o -> {
            if (o instanceof Integer) {
                return ((Integer) o).intValue() + 2;
            }
            return null;
        };

        assertEquals(socketTest.getCallbackFunctions().size(), 0);
        socketTest.on("open", func);

        final List<Function> fnList = socketTest.getCallbackFunctions().get("open");
        assertEquals(fnList.size(), 1);

        final Object response = fnList.get(0).apply(new Integer(2));
        assertEquals(response, 4);
        assertEquals(socketTest.getCallbackFunctions().size(), 1);
    }

    @Test
    public void testHancockSocketWatchTransfer() {

        address.add("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        socket_spy.watchTransfer(address);

        verify(socket_spy, times(1)).sendMessage(eq(HancockSocketMessageRequestKind.WATCHTRANSFER.getKind()), eq(address));
    }

    @Test
    public void testHancockSocketWatchTransaction() {

        address.add("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        socket_spy.watchTransaction(address);

        verify(socket_spy, times(1)).sendMessage(eq(HancockSocketMessageRequestKind.WATCHTRANSACTION.getKind()), eq(address));
    }

    @Test
    public void testHancockSocketWatchContractEvents() {

        contracts.add("test_contract");

        socket_spy.watchContractEvent(contracts);

        verify(socket_spy, times(1)).sendMessage(eq(HancockSocketMessageRequestKind.WATCHSMARTCONTRACTEVENT.getKind()), eq(contracts));
    }

    @Test
    public void testHancockSocketWatchContractTransaction() {

        contracts.add("test_contract");

        socket_spy.watchContractTransaction(contracts);

        verify(socket_spy, times(1)).sendMessage(eq(HancockSocketMessageRequestKind.WATCHSMARTCONTRACTTRANSACTION.getKind()), eq(contracts));
    }

    @Test
    public void testHancockSocketWatchTransferWithNoParams() {

        socket_spy.watchTransfer(address);

        verify(socket_spy, times(0)).sendMessage(any(String.class), any(ArrayList.class));
    }

    @Test
    public void testHancockSocketWatchTransactionWithNoParams() {

        socket_spy.watchTransaction(address);

        verify(socket_spy, times(0)).sendMessage(any(String.class), any(ArrayList.class));
    }

    @Test
    public void testHancockSocketWatchContractTransactionWithNoParams() {

        socket_spy.watchContractTransaction(address);

        verify(socket_spy, times(0)).sendMessage(any(String.class), any(ArrayList.class));
    }

    @Test
    public void testHancockSocketWatchContractEventWithNoParams() {

        socket_spy.watchContractEvent(address);

        verify(socket_spy, times(0)).sendMessage(any(String.class), any(ArrayList.class));
    }

    @Test
    public void testHancockSocketUnwatchTransfer() {

        address.add("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        socket_spy.unwatchTransfer(address);

        verify(socket_spy, times(1)).sendMessage(eq(HancockSocketMessageRequestKind.UNWATCHTRANSFER.getKind()), eq(address));
    }

    @Test
    public void testHancockSocketUnwatchTransaction() {

        address.add("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        socket_spy.unwatchTransaction(address);

        verify(socket_spy, times(1)).sendMessage(eq(HancockSocketMessageRequestKind.UNWATCHTRANSACTION.getKind()), eq(address));
    }

    @Test
    public void testHancockSocketUnwatchContractsTransaction() {

        contracts.add("test_contract");

        socket_spy.unwatchContractTransaction(contracts);

        verify(socket_spy, times(1)).sendMessage(eq(HancockSocketMessageRequestKind.UNWATCHSMARTCONTRACTTRANSACTION.getKind()), eq(contracts));
    }

    @Test
    public void testHancockSocketUnwatchContractsEvent() {

        contracts.add("test_contract");

        socket_spy.unwatchContractEvent(contracts);

        verify(socket_spy, times(1)).sendMessage(eq(HancockSocketMessageRequestKind.UNWATCHSMARTCONTRACTEVENT.getKind()), eq(contracts));
    }

    @Test
    public void testHancockSocketUnwatchTransferWithNoParams() {

        socket_spy.unwatchTransfer(address);

        verify(socket_spy, times(0)).sendMessage(any(String.class), any(ArrayList.class));
    }

    @Test
    public void testHancockSocketUnwatchTransactionWithNoParams() {

        socket_spy.unwatchTransaction(address);

        verify(socket_spy, times(0)).sendMessage(any(String.class), any(ArrayList.class));
    }

    @Test
    public void testHancockSocketUnwatchContractsTransactionWithNoParams() {

        socket_spy.unwatchContractTransaction(address);

        verify(socket_spy, times(0)).sendMessage(any(String.class), any(ArrayList.class));
    }

    @Test
    public void testHancockSocketUnwatchContractsEventWithNoParams() {

        socket_spy.unwatchContractEvent(address);

        verify(socket_spy, times(0)).sendMessage(any(String.class), any(ArrayList.class));
    }

    @Test
    public void testHancockSocketonError() {

        final Function<Exception, Void> func = (Exception e) -> {
            return null;
        };
        assertEquals(socketTest.getErrorFunctions().size(), 0);
        socket_spy.onError(func);
        assertEquals(socketTest.getErrorFunctions().size(), 1);
    }

    @Test
    public void testHancockSocketOnTransaction() {

        final Function<HancockSocketTransactionEvent, Void> func = (HancockSocketTransactionEvent e) -> {
            return null;
        };
        assertEquals(socketTest.getTransactionFunctions().size(), 0);
        socket_spy.onTransaction("testTransaction", func);
        assertEquals(socketTest.getTransactionFunctions().size(), 1);
        assertEquals(socketTest.getTransactionFunctions().get("testTransaction").get(0), func);
    }

    @Test
    public void testHancockSocketOnEvent() {

        final Function<HancockSocketContractEvent, Void> func = (HancockSocketContractEvent e) -> {
            return null;
        };
        assertEquals(socketTest.getContractsFunctions().size(), 0);
        socket_spy.onContractEvent("testEvent", func);
        assertEquals(socketTest.getContractsFunctions().size(), 1);
        assertEquals(socketTest.getContractsFunctions().get("testEvent").get(0), func);
    }

}
