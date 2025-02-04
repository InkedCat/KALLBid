package fr.umontpellier.iut.socketwrapper;

import fr.umontpellier.iut.socketwrapper.exceptions.SocketClosedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientSocketWrapperTest {

    private ByteArrayOutputStream output;
    private ByteArrayInputStream input;

    @Mock
    private Socket mockSocket;
    String inputMessage = "Hello test !";
    String outputMessage = "Hello world !";

    @BeforeEach
    public void init() throws IOException {
        output = new ByteArrayOutputStream();

        String inputNewlined = inputMessage + "\r\n";
        input = new ByteArrayInputStream(inputNewlined.getBytes());

        when(mockSocket.getInputStream()).thenReturn(input);
        when(mockSocket.getOutputStream()).thenReturn(output);
    }

    // Read tests

    @Test
    public void readShouldReturnMessageWhenOpen() throws IOException, SocketClosedException {
        try(ClientSocketWrapper wrapper = new ClientSocketWrapper(mockSocket)) {
            assertEquals(inputMessage, wrapper.read());
        }
    }

    @Test
    public void readShouldInitializeInputStreamWhenFirstRead() throws IOException, SocketClosedException {
        try(ClientSocketWrapper wrapper = new ClientSocketWrapper(mockSocket)) {
            wrapper.read();
        }

        verify(mockSocket).getInputStream();
    }

    @Test
    public void readShouldThrowSocketClosedExceptionWhenClosed() throws IOException {
        try(ClientSocketWrapper wrapper = new ClientSocketWrapper(mockSocket)) {
            wrapper.close();

            assertThrows(SocketClosedException.class, wrapper::read);
        }
    }

    @Test
    public void readShouldCloseSocketWhenEnpointDisconnect() throws IOException {
        try(ClientSocketWrapper wrapper = new ClientSocketWrapper(mockSocket)) {
            input.readAllBytes();

            assertThrows(SocketClosedException.class, wrapper::read);
            assertTrue(wrapper.isClosed());
        }

        verify(mockSocket).close();
    }


    // Send tests

    @Test
    public void sendShouldWriteMessageWhenOpen() throws SocketClosedException, IOException {
        try(ClientSocketWrapper wrapper = new ClientSocketWrapper(mockSocket)) {
            wrapper.send(outputMessage);
        }

        String expected = outputMessage + System.lineSeparator();
        assertEquals(expected, output.toString());
    }

    @Test
    public void sendShouldInitializeOutputStreamWhenFirstSend() throws SocketClosedException, IOException {
        try(ClientSocketWrapper wrapper = new ClientSocketWrapper(mockSocket)) {
            wrapper.send(outputMessage);
        }

        verify(mockSocket).getOutputStream();
    }

    @Test
    public void sendShouldThrowSocketClosedExceptionWhenClosed() throws IOException {
        try(ClientSocketWrapper wrapper = new ClientSocketWrapper(mockSocket)) {
            wrapper.close();

            assertThrows(SocketClosedException.class, () -> wrapper.send(outputMessage));
        }
    }

    // Close tests

    @Test
    public void closeShouldCloseSocket() throws IOException {
        try(ClientSocketWrapper wrapper = new ClientSocketWrapper(mockSocket)) {
            assertDoesNotThrow(wrapper::close);
            assertTrue(wrapper.isClosed());
        }

        verify(mockSocket).close();
    }

    @Test
    public void closeShouldNotThrowWhenAlreadyClosed() throws IOException {
        try(ClientSocketWrapper wrapper = new ClientSocketWrapper(mockSocket)) {
            wrapper.close();

            assertTrue(wrapper.isClosed());
            assertDoesNotThrow(wrapper::close);
        }
    }

    @Test
    public void closeShouldNotThrowWhenSocketCloseThrowException() throws IOException {
        doThrow(IOException.class).when(mockSocket).close();

        try(ClientSocketWrapper wrapper = new ClientSocketWrapper(mockSocket)) {
            assertDoesNotThrow(wrapper::close);
            assertTrue(wrapper.isClosed());
        }
    }
}
