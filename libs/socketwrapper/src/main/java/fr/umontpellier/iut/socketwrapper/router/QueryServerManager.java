package fr.umontpellier.iut.socketwrapper.router;

import fr.umontpellier.iut.queryhelper.Query;
import fr.umontpellier.iut.queryhelper.QueryCouple;
import fr.umontpellier.iut.socketwrapper.ServerSocketWrapper;

import java.io.IOException;

public final class QueryServerManager {
    private ServerSocketWrapper auctionServer;
    private EventManager auctionEventManager;

    QueryServerManager(ServerSocketWrapper auctionServer, EventManager auctionEventManager) {
        this.auctionServer = auctionServer;
        this.auctionEventManager = auctionEventManager;
    }

    public QueryServerManager(int port, int timeout) throws IOException {
        this.auctionEventManager = new EventManager();
        this.auctionServer = new ServerSocketWrapper(port, new QueryRouterFactory(auctionEventManager), timeout);
    }

    public void addEvent(String queryType, QueryCouple<? extends Query> handler) {
        auctionEventManager.addQueryCouple(queryType, handler);
    }

    public void removeEvent(String queryType) {
        auctionEventManager.removeQueryCouple(queryType);
    }

    public void broadcast(String query) {
        auctionServer.broadcast(query);
    }

    public void stop() {
        auctionServer.close();
    }
}
