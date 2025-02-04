package fr.umontpellier.iut.socketwrapper.router;

import fr.umontpellier.iut.socketwrapper.ClientHandler;
import fr.umontpellier.iut.socketwrapper.ClientHandlerFactory;
import fr.umontpellier.iut.socketwrapper.ClientSocketWrapper;

public class QueryRouterFactory implements ClientHandlerFactory {

    private EventManager eventManager;

    public QueryRouterFactory(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public ClientHandler create(ClientSocketWrapper client) {
        return new QueryRouter(client, eventManager);
    }
}
