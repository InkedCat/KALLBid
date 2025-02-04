package fr.umontpellier.iut.socketwrapper;

import fr.umontpellier.iut.socketwrapper.exceptions.RefresherClosedException;
import fr.umontpellier.iut.socketwrapper.exceptions.RemoteUnavailableException;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionRefresher {

    private String host;
    private int port;
    private ClientSocketWrapper client;
    private ExecutorService clientService;
    private ClientHandlerFactory clientHandlerFactory;
    private int timeout;
    private boolean stopped = false;

    /**
     * Crée un nouveau ConnectionRefresher à partir d'un client et d'une factory de gestion des clients
     *
     * @param client le client à rafraîchir
     * @param clientHandlerFactory la factory pour gérer les communications entrantes
     * @throws SocketException si le client est fermé
     */
    protected ConnectionRefresher(ClientSocketWrapper client, ClientHandlerFactory clientHandlerFactory) throws SocketException {
        this.client = client;
        this.host = client.getRemoteSocketAddress().getHostAddress();
        this.port = client.getRemotePort();
        this.clientHandlerFactory = clientHandlerFactory;
        clientService = Executors.newSingleThreadExecutor();
        this.timeout = client.getTimeout();
    }

    /**
     * Crée un nouveau ConnectionRefresher à partir d'une adresse et un port
     *
     * @param host l'adresse de connexion du serveur
     * @param port le port du serveur
     * @param clientHandlerFactory la factory pour gérer les communications entrantes
     * @param timeout le timeout d'opération en millisecondes
     */
    public ConnectionRefresher(String host, int port, ClientHandlerFactory clientHandlerFactory, int timeout) {
        this.client = null;
        this.host = host;
        this.port = port;
        this.clientHandlerFactory = clientHandlerFactory;
        clientService = Executors.newSingleThreadExecutor();
        this.timeout = timeout;
    }

    private void reset() {
        if (client != null) client = null;

        clientService.shutdownNow();
        clientService = Executors.newSingleThreadExecutor();
    }

    private void start() throws IOException, RemoteUnavailableException {
        client = new ClientSocketWrapper(host, port, timeout);
        clientService.execute(clientHandlerFactory.create(client));
    }

    /**
     * Récupère le client rafraîchi
     *
     * @return le client rafraîchi
     * @throws RemoteUnavailableException si le serveur distant est injoignable
     * @throws IOException si une erreur survient lors de l'initialisation du client
     */
    public ClientSocketWrapper getConnection() throws RemoteUnavailableException, IOException, RefresherClosedException {
        if(stopped) throw new RefresherClosedException("Le rafraîchisseur de connexion est fermé");

        if(client == null) start();
        else if(client.isClosed()) {
            reset();
            start();
        }

        return client;
    }

    /**
     * Arrête le rafraîchissement
     */
    public void stop() {
        if(stopped) return;

        clientService.shutdownNow();

        if (client != null && !client.isClosed()) {
            client.close();
        }

        stopped = true;
    }
}
