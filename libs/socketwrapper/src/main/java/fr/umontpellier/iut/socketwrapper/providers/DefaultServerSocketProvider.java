package fr.umontpellier.iut.socketwrapper.providers;

import java.io.IOException;
import java.net.ServerSocket;



/**
 * @deprecated Use {@link SecuredClientSocketProvider} instead
 */
@Deprecated
public class DefaultServerSocketProvider implements ServerSocketProvider {
    @Override
    public ServerSocket createServerSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        return serverSocket;
    }
}
