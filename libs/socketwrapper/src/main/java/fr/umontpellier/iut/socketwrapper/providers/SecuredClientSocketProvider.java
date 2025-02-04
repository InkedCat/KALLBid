package fr.umontpellier.iut.socketwrapper.providers;

import nl.altindag.ssl.SSLFactory;

import java.io.IOException;
import java.net.Socket;


public class SecuredClientSocketProvider implements ClientSocketProvider {

    private int timeout;

    public SecuredClientSocketProvider(int timeout) {
        if(timeout < 0) throw new IllegalArgumentException("timeout ne peut pas être négatif");
        this.timeout = timeout;
    }

    @Override
    public Socket createClientSocket() throws IOException {

        SSLFactory sslFactory = SSLFactory.builder()
                .withSslContextAlgorithm("TLS")
                .withDefaultTrustMaterial()
                .withTrustMaterial(getClass().getResourceAsStream("/certs/r3.pfx"), null)
                .build();

        Socket socket = sslFactory.getSslContext().getSocketFactory().createSocket();
        socket.setSoTimeout(timeout);
        return socket;
    }
}
