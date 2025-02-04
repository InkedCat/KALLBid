package fr.umontpellier.iut.socketwrapper;

import fr.umontpellier.iut.socketwrapper.providers.ServerSocketProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ServerSocketWrapperTest {

    @Mock
    private ServerSocket mockServerSocket;
    @Mock
    private Socket mockSocket;
    @Mock
    private ClientHandlerFactory mockClientHandlerFactory;
    @Mock
    private ClientHandler mockClientHandler;
    @Mock
    private ExecutorService mockExecutorService;
    @Mock
    private ServerSocketProvider mockServerSocketProvider;


    @BeforeEach
    public void init() throws IOException {
        when(mockServerSocketProvider.createServerSocket()).thenReturn(mockServerSocket);
        doNothing().when(mockServerSocket).bind(any(InetSocketAddress.class));

        lenient().when(mockClientHandlerFactory.create(any(ClientSocketWrapper.class))).thenReturn(mockClientHandler);

        lenient().when(mockServerSocket.accept()).thenReturn(mockSocket);
    }

    @Test
    public void closeShouldCloseServerSocket() throws IOException {
        try(ServerSocketWrapper serverSocketWrapper = new ServerSocketWrapper(0, mockClientHandlerFactory, mockExecutorService, mockServerSocketProvider)) {
            assertDoesNotThrow(serverSocketWrapper::close);
            assertTrue(serverSocketWrapper.isClosed());
        }

        verify(mockServerSocket).close();
    }

    @Test
    public void closeShouldCloseExecutorService() throws InterruptedException, IOException {
        try(ServerSocketWrapper serverSocketWrapper = new ServerSocketWrapper(0, mockClientHandlerFactory, mockExecutorService, mockServerSocketProvider)) {
            assertDoesNotThrow(serverSocketWrapper::close);
            assertTrue(serverSocketWrapper.isClosed());
        }

        verify(mockExecutorService).shutdown();
        verify(mockExecutorService).awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
        verify(mockExecutorService).shutdownNow();
    }

    @Test
    public void closeShouldCloseClientHandler() throws IOException {
        try (ServerSocketWrapper serverSocketWrapper = new ServerSocketWrapper(0, mockClientHandlerFactory, mockExecutorService, mockServerSocketProvider)) {
            assertDoesNotThrow(serverSocketWrapper::close);
            assertTrue(serverSocketWrapper.isClosed());
            assertFalse(serverSocketWrapper.isListening());
        }
    }

    @Test
    public void closeShouldCloseServerSocketWrapper() throws IOException {
        try (ServerSocketWrapper serverSocketWrapper = new ServerSocketWrapper(0, mockClientHandlerFactory, mockExecutorService, mockServerSocketProvider)) {
            assertFalse(serverSocketWrapper.isClosed());
            assertDoesNotThrow(serverSocketWrapper::close);
            assertTrue(serverSocketWrapper.isClosed());
        }
    }
}
