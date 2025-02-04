package fr.umontpellier.iut.socketwrapper;

import fr.umontpellier.iut.socketwrapper.exceptions.RemoteUnavailableException;
import fr.umontpellier.iut.socketwrapper.providers.ClientSocketProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientSocketWrapperConstructorTest {


    @Mock
    private Socket mockSocket;
    @Mock
    private InetSocketAddress mockInetSocketAddress;
    @Mock
    private ClientSocketProvider mockClientSocketProvider;
    @Mock
    private OutputStream output;
    @Mock
    private InputStream input;


    // Injections tests
    @Test
    public void createSocketWrapperShouldNotThrowWhenSocketIsOpen() throws IOException {
        when(mockSocket.isClosed()).thenReturn(false);
        when(mockSocket.getInputStream()).thenReturn(input);
        when(mockSocket.getOutputStream()).thenReturn(output);

        assertDoesNotThrow(() -> new ClientSocketWrapper(mockSocket));

        verify(mockSocket).getInputStream();
        verify(mockSocket).getOutputStream();
    }

    @Test
    public void createSocketWrapperShouldThrowNullPointerExceptionWhenSocketIsNull() {
        assertThrows(NullPointerException.class, () -> new ClientSocketWrapper(null));
    }

    @Test
    public void createSocketWrapperShouldThrowIllegalArgumentExceptionWhenSocketIsClosed(){
        when(mockSocket.isClosed()).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> new ClientSocketWrapper(mockSocket));
    }

    @Test
    public void createSocketWrapperShouldThrowIOExceptionWhenSocketStreamsThrowExceptions() throws IOException {
        when(mockSocket.isClosed()).thenReturn(false);
        when(mockSocket.getInputStream()).thenThrow(IOException.class);

        assertThrows(IOException.class, () -> new ClientSocketWrapper(mockSocket));
    }

    @Test
    public void createSocketWrapperShouldNotThrowWhenHostIsReachable() throws IOException {
        when(mockInetSocketAddress.isUnresolved()).thenReturn(false);
        when(mockClientSocketProvider.createClientSocket()).thenReturn(mockSocket);
        when(mockSocket.isConnected()).thenReturn(true);
        when(mockSocket.getInputStream()).thenReturn(input);
        when(mockSocket.getOutputStream()).thenReturn(output);

        assertDoesNotThrow(() -> new ClientSocketWrapper(mockInetSocketAddress, mockClientSocketProvider));
    }

    @Test
    public void createSocketWrapperShouldThrowNullPointerExceptionWhenDistantAddressIsNull() {
        assertThrows(NullPointerException.class, () -> new ClientSocketWrapper(null, mockClientSocketProvider));
    }

    @Test
    public void createSocketWrapperShouldThrowIOExceptionWhenDistantAddressIsUnresolved() {
        when(mockInetSocketAddress.isUnresolved()).thenReturn(true);
        assertThrows(IOException.class, () -> new ClientSocketWrapper(mockInetSocketAddress, mockClientSocketProvider));
    }

    @Test
    public void createSocketWrapperShouldThrowNullPointerExceptionWhenClientSocketProviderIsNull() {
        when(mockInetSocketAddress.isUnresolved()).thenReturn(false);
        assertThrows(NullPointerException.class, () -> new ClientSocketWrapper(mockInetSocketAddress, null));
    }

    @Test
    public void createSocketWrapperShouldThrowNullPointerExceptionWhenClientSocketProviderReturnNull() throws IOException {
        when(mockInetSocketAddress.isUnresolved()).thenReturn(false);
        when(mockClientSocketProvider.createClientSocket()).thenReturn(null);
        assertThrows(NullPointerException.class, () -> new ClientSocketWrapper(mockInetSocketAddress, mockClientSocketProvider));
    }

    @Test
    public void createSocketWrapperShouldThrowRemoteUnavailableExceptionWhenHostIsNotReachable() {
        assertThrows(RemoteUnavailableException.class, () -> new ClientSocketWrapper("127.0.0.1", 0, 1000));
    }
}
