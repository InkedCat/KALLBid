package fr.umontpellier.iut.socketwrapper;

import fr.umontpellier.iut.socketwrapper.exceptions.RemoteUnavailableException;
import fr.umontpellier.iut.socketwrapper.exceptions.SocketClosedException;
import fr.umontpellier.iut.socketwrapper.providers.SecuredServerSocketProvider;
import fr.umontpellier.iut.socketwrapper.providers.ServerSocketProvider;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;

public final class ServerSocketWrapper implements SocketWrapper {
    private ServerSocket server;
    private final int port;
    private ServerSocketProvider serverSocketProvider;
    private Thread clientConnectionHandler;
    private ExecutorService pool;
    private ClientHandlerFactory handlerFactory;
    private int timeout = 0;
    private boolean isClosed = false;
    private final HashSet<ClientSocketWrapper> connectedClients = new HashSet<>();

    private static final int MAX_PORT_VALUE = 65535;

    /**
     * Crée un nouveau ServerSocketWrapper à partir d'un port, d'une factory de gestion des clients et d'un pool d'exécution
     * @param port le port à écouter (de 0 à 65536)
     * @param handlerFactory la factory pour gérer les connexions entrantes
     * @param pool le pool d'exécution pour les threads de gestion des clients
     * @param serverSocketProvider le provider pour créer le serveur
     * @throws IOException si l'instanciation du serveur a été interrompue
     */
    public ServerSocketWrapper(int port, ClientHandlerFactory handlerFactory, ExecutorService pool,
                               ServerSocketProvider serverSocketProvider) throws IOException {
        if(port < 0 || port > MAX_PORT_VALUE) throw new IllegalArgumentException("Le port de l'adresse distante doit être inclus entre 0 et 65 536");

        Objects.requireNonNull(handlerFactory, "handlerFactory ne doit pas être nulle");

        Objects.requireNonNull(pool, "pool ne doit pas être nulle");
        if(pool.isShutdown()) throw new IllegalArgumentException("Le pool d'exécution ne peut pas être fermé");
        if(pool.isTerminated()) throw new IllegalArgumentException("Le pool d'exécution ne peut pas être terminé");

        Objects.requireNonNull(serverSocketProvider, "serverSocketProvider ne doit pas être nulle");

        this.port = port;
        this.handlerFactory = handlerFactory;
        this.pool = pool;
        this.serverSocketProvider = serverSocketProvider;

        initializeServer();
        initializeClientConnectionHandler();
    }

    /**
     * Crée un nouveau ServerSocketWrapper à partir d'un port et d'une factory de gestion des clients
     * @param port le port à écouter (de 0 à 65536)
     * @param handlerFactory la factory pour gérer les connexions entrantes
     * @param timeout le timeout d'opération en millisecondes
     * @throws IOException si l'instanciation du serveur a été interrompue
     */
    public ServerSocketWrapper(int port, ClientHandlerFactory handlerFactory, int timeout) throws IOException {
        this(port, handlerFactory, Executors.newCachedThreadPool(), new SecuredServerSocketProvider(timeout));
        this.timeout = timeout;
    }

    private void subscribeClient(ClientSocketWrapper client) {
        synchronized (connectedClients) {
            connectedClients.add(client);
        }
    }

    private void unsubscribeClient(ClientSocketWrapper client) {
        synchronized (connectedClients) {
            connectedClients.remove(client);
        }
    }

    private class ClientConnectionHandler implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted() && !isClosed()) {
                try {
                    Socket socket = server.accept();
                    socket.setSoTimeout(timeout);
                    ClientSocketWrapper client = new ClientSocketWrapper(socket);

                    ClientHandler handler = handlerFactory.create(client);

                    CompletableFuture.runAsync(handler, pool)
                            .thenAccept(result -> {
                                unsubscribeClient(client);
                            }).exceptionally(throwable -> {
                                unsubscribeClient(client);
                                client.close();
                                return null;
                            });

                    subscribeClient(client);
                } catch (IOException ignored){}
            }
        }
    }

    /**
     * Retourne l'état de fermture du serveur
     * @return true si le serveur est fermé
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * Retourne l'état d'écoute du serveur
     * @return true si le serveur écoute
     */
    public boolean isListening() {
        return clientConnectionHandler.isAlive();
    }

    /**
     * Envoie un message à tous les clients connectés
     */
    public void broadcast(String message) {
        synchronized (connectedClients) {
            for(ClientSocketWrapper client : connectedClients) {

                try {
                    client.send(message);
                } catch (SocketClosedException ignored) {}
            }
        }
    }

    private void initializeServer() throws IOException {
        server = serverSocketProvider.createServerSocket();
        server.bind(new InetSocketAddress(port));
    }

    private void initializeClientConnectionHandler() {
        clientConnectionHandler = new Thread(new ClientConnectionHandler());
        clientConnectionHandler.start();
    }

    private void closeConnections() {
        synchronized (connectedClients) {
            for(ClientSocketWrapper client : connectedClients) {
                client.close();
            }
        }
    }

    private void shutdownPool() {
        pool.shutdown();
        try {
            if(!pool.awaitTermination(5, SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
        }
    }

    private void shutdownClientHandler() {
        clientConnectionHandler.interrupt();
        try {
            clientConnectionHandler.join();
        } catch (InterruptedException ignored) {}
    }

    private void closeServer() {
        try {
            server.close();
        } catch (IOException ignore) {}
    }

    /**
     * Ferme le serveur et libère ses ressources
     */
    @Override
    public void close() {
        if(isClosed()) {
            return;
        }

        closeServer();
        shutdownClientHandler();
        closeConnections();
        shutdownPool();
        isClosed = true;
    }
}
