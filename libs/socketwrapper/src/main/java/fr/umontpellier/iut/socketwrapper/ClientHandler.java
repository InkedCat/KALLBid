package fr.umontpellier.iut.socketwrapper;

import java.util.Objects;

/**
 * Cette classe permet de gérer un client connecté au serveur
 * @see ClientHandlerFactory
 * @see ServerSocketWrapper
 */
public abstract class ClientHandler implements Runnable {
    private ClientSocketWrapper client;

    public ClientHandler(ClientSocketWrapper client) {
        this.client = Objects.requireNonNull(client);
    }

    protected ClientSocketWrapper getClient() {
        return client;
    }

    /**
     * Cette méthode est appelée tant que le client est connecté et que le serveur est actif
     */
    public abstract void loop();

    /**
     * Cette méthode est appelée lorsque le client se connecte au serveur
     * @see ClientSocketWrapper#isClosed()
     */
    @Override
    public void run() {
        while(!Thread.interrupted() && !getClient().isClosed()) {
            loop();
        }

        getClient().close();
    }
}
