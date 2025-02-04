package fr.umontpellier.iut.vendeur.auction;

import fr.umontpellier.iut.enchven.BiddingQuery;
import fr.umontpellier.iut.enchven.CheckBidsQuery;
import fr.umontpellier.iut.enchven.CheckWinnerQuery;
import fr.umontpellier.iut.queryhelper.QueryCouple;
import fr.umontpellier.iut.queryhelper.QuerySerializer;
import fr.umontpellier.iut.authclientforge.profile.AuthProfile;
import fr.umontpellier.iut.shared.Bid;
import fr.umontpellier.iut.shared.SignedQueryValidator;
import fr.umontpellier.iut.socketwrapper.ConnectionRefresher;
import fr.umontpellier.iut.socketwrapper.exceptions.RefresherClosedException;
import fr.umontpellier.iut.socketwrapper.exceptions.RemoteUnavailableException;
import fr.umontpellier.iut.socketwrapper.exceptions.SocketClosedException;
import fr.umontpellier.iut.socketwrapper.router.EventManager;
import fr.umontpellier.iut.socketwrapper.router.QueryServerManager;
import fr.umontpellier.iut.venaut.FindWinnerQuery;
import fr.umontpellier.iut.venaut.SendWinnerQuery;
import fr.umontpellier.iut.vendeur.bids.BidCouple;
import fr.umontpellier.iut.vendeur.bids.BidListService;
import fr.umontpellier.iut.vendeur.bids.strategies.BidListStrategy;
import fr.umontpellier.iut.vendeur.bids.strategies.OldestStrategy;
import fr.umontpellier.iut.vendeur.bids.strategies.SimpleStrategy;
import fr.umontpellier.iut.vendeur.builder.CheckBidsQueryBuilder;
import fr.umontpellier.iut.vendeur.builder.CheckWinnerQueryBuilder;
import fr.umontpellier.iut.vendeur.builder.FindWinnerQueryBuilder;
import fr.umontpellier.iut.vendeur.handler.BiddingQueryHandler;
import fr.umontpellier.iut.vendeur.handler.SendWinnerQueryHandler;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AuctionManager {
    private Auction auction;
    private QueryServerManager auctionServer;
    private SignedQueryValidator signedQueryValidator;
    private CheckBidsQueryBuilder checkBidsQueryBuilder;
    private CheckWinnerQueryBuilder checkWinnerQueryBuilder;
    private FindWinnerQueryBuilder findWinnerQueryBuilder;
    private BidListService bidListService;
    private BidListStrategy anonymisedStrategy;
    private BidListStrategy anonymisedOldestStrategy;
    private ConnectionRefresher autorite;
    private EventManager autoriteEventManager;
    private AuthProfile authProfile;

    private void addAuctionEvents() {
        auctionServer.addEvent("bidding", new QueryCouple<>(BiddingQuery.class,
                        new BiddingQueryHandler(auction, signedQueryValidator)));
    }

    private void removeAuctionEvents() {
        auctionServer.removeEvent("bidding");
    }

    private void addAutoriteEvents() {
        autoriteEventManager.addQueryCouple("sendWinner", new QueryCouple<>(SendWinnerQuery.class,
                        new SendWinnerQueryHandler(auction, signedQueryValidator,
                                new OldestStrategy(new SimpleStrategy()))));
    }

    public AuctionManager(QueryServerManager server, Auction auction, ConnectionRefresher autorite,
                          EventManager autoriteEventManager, SignedQueryValidator signedQueryValidator,
                          CheckBidsQueryBuilder checkBidsQueryBuilder, CheckWinnerQueryBuilder checkWinnerQueryBuilder,
                          FindWinnerQueryBuilder findWinnerQueryBuilder, BidListStrategy anonymisedStrategy,
                          AuthProfile authProfile) {
        this.auction = auction;
        this.auctionServer = server;

        this.signedQueryValidator = signedQueryValidator;
        this.checkBidsQueryBuilder = checkBidsQueryBuilder;
        this.checkWinnerQueryBuilder = checkWinnerQueryBuilder;
        this.findWinnerQueryBuilder = findWinnerQueryBuilder;

        this.anonymisedStrategy = anonymisedStrategy;
        this.anonymisedOldestStrategy = new OldestStrategy(anonymisedStrategy);
        bidListService = auction.getBidListService();

        this.autorite = autorite;
        this.autoriteEventManager = autoriteEventManager;

        this.authProfile = authProfile;

        addAuctionEvents();
        addAutoriteEvents();
    }

    private boolean broadcastList() {
        List<Bid> bids = bidListService.getToBidList(anonymisedStrategy);

        Optional<CheckBidsQuery> query = checkBidsQueryBuilder.build(bids);

        if(query.isEmpty()) return false;

        auctionServer.broadcast(new QuerySerializer<CheckBidsQuery>().serialize(query.get()));
        return true;
    }

    private boolean broadcastWinnerPrice(int winnerPrice) {
        Optional<CheckWinnerQuery> query = checkWinnerQueryBuilder.build(winnerPrice);

        if(query.isEmpty()) return false;

        auctionServer.broadcast(new QuerySerializer<CheckWinnerQuery>().serialize(query.get()));
        return true;
    }

    private void waitMissingBids() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {}

        removeAuctionEvents();
    }

    private boolean askForWinner() throws RemoteUnavailableException,
            IOException, SocketClosedException, RefresherClosedException {
        List<Bid> bids = bidListService.getToBidList(anonymisedOldestStrategy);

        Optional<FindWinnerQuery> query = findWinnerQueryBuilder.build(bids, authProfile.getToken().get());

        if(query.isEmpty()) return false;

        autorite.getConnection().send(new QuerySerializer<FindWinnerQuery>().serialize(query.get()));
        return true;
    }

    public Optional<AuctionWinner> findWinner() throws RefresherClosedException, SocketClosedException, RemoteUnavailableException, IOException {
        if(auction.isFinished()) return auction.getWinner();

        if(authProfile.isAuthenticated()){
            if(!broadcastList()) return Optional.empty();

            waitMissingBids();

            if(!auction.hasBids()) {
                addAuctionEvents();
                return Optional.empty();
            }

            try {
                if(!askForWinner()) return Optional.empty();
            } catch (SocketClosedException | RemoteUnavailableException | RefresherClosedException | IOException e) {
                addAuctionEvents();
                throw e;
            }

            Optional<AuctionWinner> winner = auction.getWinnerWait();

            if(winner.isPresent()) {
                if(!broadcastWinnerPrice(winner.get().getSecondPrice()))
                    {
                        addAuctionEvents();
                        return Optional.empty();
                    }
            } else {
                addAuctionEvents();
            }

            return winner;
        }

        return Optional.empty();
    }

    public List<BidCouple> consultBids() {
        return auction.getBidListService().getBidCouples(new SimpleStrategy());
    }

    public void subscribeToBids(BiddingObserver observer) {
        auction.addObserver(observer);
    }

    public void clean() {
        autorite.stop();
        auctionServer.stop();
    }
}
