package fr.umontpellier.iut.socketwrapper;

import fr.umontpellier.iut.socketwrapper.providers.ServerSocketProvider;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServerSocketWrapperConstructorTest {

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

    @Test
    public void constructorShouldNotThrowException() throws Exception {
        when(mockServerSocketProvider.createServerSocket()).thenReturn(mockServerSocket);
        doNothing().when(mockServerSocket).bind(any(InetSocketAddress.class));
        lenient().when(mockServerSocket.accept()).thenThrow(new IOException());

        assertDoesNotThrow(() -> new ServerSocketWrapper(0, mockClientHandlerFactory, mockExecutorService, mockServerSocketProvider));

        verify(mockServerSocketProvider, times(1)).createServerSocket();
        verify(mockServerSocket).bind(any(InetSocketAddress.class));
    }

    @Test
    public void constructorShouldThrowExceptionWhenPortIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new ServerSocketWrapper(-1, mockClientHandlerFactory, mockExecutorService, mockServerSocketProvider));
    }

    @Test
    public void constructorShouldThrowExceptionWhenPortIsTooHigh() {
        assertThrows(IllegalArgumentException.class, () -> new ServerSocketWrapper(65536, mockClientHandlerFactory, mockExecutorService, mockServerSocketProvider));
    }

    @Test
    public void constructorShouldThrowExceptionWhenClientHandlerFactoryIsNull() {
        assertThrows(NullPointerException.class, () -> new ServerSocketWrapper(0, null, mockExecutorService, mockServerSocketProvider));
    }

    @Test
    public void constructorShouldThrowExceptionWhenExecutorServiceIsNull() {
        assertThrows(NullPointerException.class, () -> new ServerSocketWrapper(0, mockClientHandlerFactory, null, mockServerSocketProvider));
    }

    @Test
    public void constructorShouldThrowExceptionWhenServerSocketProviderIsNull() {
        assertThrows(NullPointerException.class, () -> new ServerSocketWrapper(0, mockClientHandlerFactory, mockExecutorService, null));
    }
}
