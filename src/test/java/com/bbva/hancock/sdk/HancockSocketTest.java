package com.bbva.hancock.sdk;

import org.java_websocket.client.WebSocketClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@PowerMockIgnore("javax.net.ssl.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({WebSocketClient.class})
public class HancockSocketTest {

    @Test
    public void testHancockSocket()throws Exception {

        HancockSocket socketTest = new HancockSocket("http://localhost:3000");

        assertEquals(new URI("http://localhost:3000"), socketTest.getWs().getURI());
        assertEquals(0, socketTest.getCallbackFunctions().size());
        assertTrue("HancockSocket create correctly", socketTest instanceof HancockSocket);
    }

    @Test
    public void testHancockSocketOn()throws Exception {

        Function func = o -> {
            if(o instanceof Integer){
                return ((Integer) o).intValue() + 2;
            }
            return null;
        };

        HancockSocket socketTest = new HancockSocket("http://localhost:3000");
        socketTest.on("open", func);

        final List<Function> fnList = socketTest.getCallbackFunctions().get("open");
        assertEquals(fnList.size(), 1);

        Object response = fnList.get(0).apply(new Integer(2));
        assertEquals(response, 4);
        assertEquals(socketTest.getCallbackFunctions().size(), 1);
    }

    @Test
    public void testHancockSocketAddContracts()throws Exception {

        ArrayList<String> contracts = new ArrayList<>();
        contracts.add("test_contract");

        HancockSocket socketTest = new HancockSocket("http://localhost:3000");
        HancockSocket socket_spy = spy(socketTest);

        PowerMockito.doNothing().when(socket_spy).sendMessage(any(String.class), any(ArrayList.class));
        socket_spy.addContract(contracts);

        verify(socket_spy, times(1)).sendMessage(eq("watch-contracts"), eq(contracts));
    }

    @Test
    public void testHancockSocketAddTransfer()throws Exception {

        ArrayList<String> address = new ArrayList<>();
        address.add("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        HancockSocket socketTest = new HancockSocket("http://localhost:3000");
        HancockSocket socket_spy = spy(socketTest);

        PowerMockito.doNothing().when(socket_spy).sendMessage(any(String.class), any(ArrayList.class));
        socket_spy.addTransfer(address);

        verify(socket_spy, times(1)).sendMessage(eq("watch-transfers"), eq(address));
    }

    @Test
    public void testHancockSocketAddTransaction()throws Exception {

        ArrayList<String> address = new ArrayList<>();
        address.add("0xde8e772f0350e992ddef81bf8f51d94a8ea9216d");

        HancockSocket socketTest = new HancockSocket("http://localhost:3000");
        HancockSocket socket_spy = spy(socketTest);

        PowerMockito.doNothing().when(socket_spy).sendMessage(any(String.class), any(ArrayList.class));
        socket_spy.addTransaction(address);

        verify(socket_spy, times(1)).sendMessage(eq("watch-transactions"), eq(address));
    }

    @Test
    public void testHancockSocketAddContractsWithNoParams()throws Exception {

        ArrayList<String> contracts = new ArrayList<>();

        HancockSocket socketTest = new HancockSocket("http://localhost:3000");
        HancockSocket socket_spy = spy(socketTest);

        PowerMockito.doNothing().when(socket_spy).sendMessage(any(String.class), any(ArrayList.class));
        socket_spy.addContract(contracts);

        verify(socket_spy, times(0)).sendMessage(any(String.class), any(ArrayList.class));
    }

    @Test
    public void testHancockSocketAddTransferWithNoParams()throws Exception {

        ArrayList<String> address = new ArrayList<>();

        HancockSocket socketTest = new HancockSocket("http://localhost:3000");
        HancockSocket socket_spy = spy(socketTest);

        PowerMockito.doNothing().when(socket_spy).sendMessage(any(String.class), any(ArrayList.class));
        socket_spy.addTransfer(address);

        verify(socket_spy, times(0)).sendMessage(any(String.class), any(ArrayList.class));
    }

    @Test
    public void testHancockSocketAddTransactionWithNoParams()throws Exception {

        ArrayList<String> address = new ArrayList<>();

        HancockSocket socketTest = new HancockSocket("http://localhost:3000");
        HancockSocket socket_spy = spy(socketTest);

        PowerMockito.doNothing().when(socket_spy).sendMessage(any(String.class), any(ArrayList.class));
        socket_spy.addTransaction(address);

        verify(socket_spy, times(0)).sendMessage(any(String.class), any(ArrayList.class));
    }
}
