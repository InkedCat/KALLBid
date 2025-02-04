package fr.umontpellier.iut.socketwrapper.providers;

import java.io.IOException;
import java.net.Socket;

/**
 * @deprecated Use {@link SecuredClientSocketProvider} instead
 */
@Deprecated
public class DefaultClientSocketProvider implements ClientSocketProvider{
    @Override
    public Socket createClientSocket() {
        return new Socket();
    }
}
