package fr.umontpellier.iut.socketwrapper.providers;

import nl.altindag.ssl.SSLFactory;

import javax.net.ssl.SSLServerSocket;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;


public class SecuredServerSocketProvider implements ServerSocketProvider {

    private int timeout;

    public SecuredServerSocketProvider(int timeout) {
        if(timeout < 0) throw new IllegalArgumentException("timeout ne peut pas être négatif");
        this.timeout = timeout;
    }

    @Override
    public ServerSocket createServerSocket() throws IOException {

        if(System.getenv("SERVER_CERT_PATH") == null || System.getenv("SERVER_CERT_PASSWORD") == null)
            throw new IllegalArgumentException("SERVER_CERT_PATH and SERVER_CERT_PASSWORD must be set as environment variables");

        SSLFactory sslFactory = SSLFactory.builder()
                .withSslContextAlgorithm("TLS")
                .withIdentityMaterial(new FileInputStream(System.getenv("SERVER_CERT_PATH")), System.getenv("SERVER_CERT_PASSWORD").toCharArray())
                .build();

        SSLServerSocket serverSocket = (SSLServerSocket) sslFactory.getSslContext().getServerSocketFactory().createServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.setSoTimeout(timeout);
        return serverSocket;
    }
}
