package fr.umontpellier.iut.socketwrapper.providers;

import java.io.IOException;
import java.net.Socket;

public interface ClientSocketProvider {
    Socket createClientSocket() throws IOException;
}
