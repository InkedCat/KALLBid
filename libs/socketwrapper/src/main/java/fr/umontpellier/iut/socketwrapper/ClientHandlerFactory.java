package fr.umontpellier.iut.socketwrapper;

/**
 * Cette classe définit une factory permettant de créer des {@link ClientHandler}<br />
 * @see ClientHandler
 * @see ServerSocketWrapper
 */
public interface ClientHandlerFactory {
    ClientHandler create(ClientSocketWrapper client);
}
