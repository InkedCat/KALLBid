package fr.umontpellier.iut.socketwrapper;

import fr.umontpellier.iut.socketwrapper.exceptions.RemoteUnavailableException;
import fr.umontpellier.iut.socketwrapper.exceptions.SocketClosedException;
import fr.umontpellier.iut.socketwrapper.providers.ClientSocketProvider;
import fr.umontpellier.iut.socketwrapper.providers.SecuredClientSocketProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Objects;

public final class ClientSocketWrapper implements SocketWrapper{

    private ClientSocketProvider clientSocketProvider;
    private Socket client;
    private boolean isClosed = false;

    private OutputStream outputStream;
    private InputStream inputStream;

    private PrintWriter out;
    private BufferedReader in;

    ClientSocketWrapper(Socket client) throws IOException {
        Objects.requireNonNull(client, "le socket ne doit pas être nul");
        if(client.isClosed()) throw new IllegalArgumentException("Le socket ne peut pas être fermé");

        this.client = client;

        tryInitializeStreams();
    }

    /**
     * Initialise un nouveau ClientSocketWrapper à partir d'une adresse et d'un provider de socket
     * @param distantAddress l'adresse de connexion du serveur
     * @param clientSocketProvider le provider de socket
     * @throws RemoteUnavailableException si le serveur distant est injoignable
     * @throws IOException si une erreur survient lors de l'initialisation des flux
     */
    public ClientSocketWrapper(InetSocketAddress distantAddress, ClientSocketProvider clientSocketProvider) throws RemoteUnavailableException,IOException  {
        Objects.requireNonNull(distantAddress, "l'adresse distante ne doit pas être nulle");
        if(distantAddress.isUnresolved()) throw new IOException("Impossible de résoudre l'adresse distante");

        Objects.requireNonNull(clientSocketProvider, "le provider de socket ne doit pas être nul");
        this.clientSocketProvider = clientSocketProvider;
        client = clientSocketProvider.createClientSocket();
        Objects.requireNonNull(client, "le socket créé par le provider ne doit pas être nul");

        tryConnectSocket(distantAddress);

        tryInitializeStreams();
    }

    /**
     * Initialise un nouveau ClientSocketWrapper à partir d'une adresse et un port
     * @param host l'adresse de connexion du serveur
     * @param port le port du serveur
     * @param timeout le timeout d'opération en millisecondes
     * @throws RemoteUnavailableException si le serveur distant est injoignable
     * @throws IOException si une erreur survient lors de l'initialisation des flux
     */
    public ClientSocketWrapper(String host, int port, int timeout) throws RemoteUnavailableException, IOException {
        this(new InetSocketAddress(host, port), new SecuredClientSocketProvider(timeout));
    }

    /**
     * Retourne l'état du socket
     *
     * @return true si le socket est fermé
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * Retourne l'adresse distante du socket
     *
     * @return l'adresse distante du socket
     */
    public InetAddress getRemoteSocketAddress() {
        return client.getInetAddress();
    }

    /**
     * Retourne le port distant du socket
     *
     * @return le port distant du socket
     */
    public int getRemotePort() {
        return client.getPort();
    }

    public int getTimeout() throws SocketException {
        return client.getSoTimeout();
    }

    private void resetConnection() {
        try {
            client.close();
        } catch (IOException ignored) {}
        finally {
            try {client = clientSocketProvider.createClientSocket();} catch (IOException ignored) {}
        }
    }

    private void tryConnectSocket(InetSocketAddress distantAddress) throws RemoteUnavailableException {
        int retries = 5;
        int backoff = 500;

        while(retries-- > 0 && !client.isConnected()){
            try {
                client.connect(distantAddress, backoff - 50);
            } catch (IOException ioException) {
                resetConnection();

                backoff *= 2;
                System.out.println("Tentative de reconnexion à: " + distantAddress + " dans " + backoff / 1000 + " secondes...");
                try {
                    Thread.sleep(backoff);

                } catch (InterruptedException interruptedException) {
                    retries = 0;
                }

                if(retries == 0) {
                    close();
                    throw new RemoteUnavailableException("Impossible de se connecter au serveur distant", ioException);
                }
            }
        }
    }

    private void tryInitializeStreams() throws IOException {
        int retries = 3;

        while(retries-- > 0 && (outputStream == null || inputStream == null)) {
            try {
                if(outputStream == null) outputStream = client.getOutputStream();
                if(inputStream == null) inputStream = client.getInputStream();
            } catch (IOException ioException) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException interruptedException) {
                    retries = 0;
                }

                if(retries == 0) {
                    close();
                    throw new IOException("Impossible d'initialiser les flux de communication");
                }
            }
        }
    }

    /**
     * Envoie un message à l'extrémité distante
     * @param message le message à envoyer
     * @throws SocketClosedException si le socket est fermé
     */
    public void send(String message) throws SocketClosedException {
        if(isClosed()) throw new SocketClosedException("Le socket est fermé");
        if(out == null) out = new PrintWriter(outputStream, true);

        out.println(message);

        if(out.checkError()) {
            close();
            throw new SocketClosedException("La connexion a été fermée par l'extrémité distante");
        }
    }

    /**
     * Lit un message de l'extrémité distante
     * @return le message lu
     * @throws IOException si une erreur survient lors de la lecture
     * @throws SocketClosedException si le socket est fermé
     */
    public String read() throws IOException, SocketClosedException {
        if(isClosed()) throw new SocketClosedException("Le socket est fermé");
        if(in == null) in = new BufferedReader(new InputStreamReader(inputStream));

        String message;
        try {
            message = in.readLine();

            if(message == null) {
                close();
                throw new SocketClosedException("La connexion a été fermée par l'extrémité distante");
            }
        } catch(IOException e) {
            if(e instanceof SocketTimeoutException) {
                throw new IOException("Le délai d'attente du socket est dépassé", e);
            }

            close();
            throw new SocketClosedException("La connexion a été fermée par l'extrémité distante", e);
        }

        return message;
    }

    /**
     * Ferme le socket et libère ses ressources
     */
    @Override
    public void close() {
        if(isClosed()) return;

        try{
            if(out != null) out.close();
            if(in != null) in.close();
            client.close();
        } catch (IOException ioException) {
            System.err.println("Erreur lors de la fermeture du socket : " + ioException.getMessage());
        } finally {
            isClosed = true;
        }
    }
}
