package fr.umontpellier.iut.socketwrapper;

import fr.umontpellier.iut.socketwrapper.exceptions.SocketClosedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientHandlerTest {

    @Mock
    private ClientSocketWrapper mockSocket;

    private ClientHandler clientHandler;

    @BeforeEach
    public void init() {
        when(mockSocket.isClosed()).thenReturn(false);

        clientHandler = new ClientHandler(mockSocket) {
            @Override
            public void loop() {
                try {
                    String message = getClient().read();
                    getClient().send(message);
                } catch (SocketClosedException | IOException e) {
                    System.out.println("The client has disconnected");
                }
            }
        };
    }


    @Test
    public void runShouldEndBeforeLoopingWhenSocketIsClosed() throws SocketClosedException, IOException {
        when(mockSocket.isClosed()).thenReturn(true);

        clientHandler.run();

        verify(mockSocket, times(0)).read();
        verify(mockSocket, times(0)).send(Mockito.anyString());
    }
}