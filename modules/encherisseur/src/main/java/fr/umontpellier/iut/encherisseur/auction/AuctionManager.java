package fr.umontpellier.iut.encherisseur.auction;

import fr.umontpellier.iut.autench.KeyQuery;
import fr.umontpellier.iut.autench.SendKeyQuery;
import fr.umontpellier.iut.cryptowrapper.encryption.exceptions.EncryptException;
import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.encherisseur.bidUtils.BidBuilder;
import fr.umontpellier.iut.encherisseur.bidUtils.LastBid;
import fr.umontpellier.iut.encherisseur.handler.CheckBidsQueryHandler;
import fr.umontpellier.iut.encherisseur.handler.CheckWinnerQueryHandler;
import fr.umontpellier.iut.encherisseur.handler.SendKeyQueryHandler;
import fr.umontpellier.iut.enchven.BiddingQuery;
import fr.umontpellier.iut.enchven.CheckBidsQuery;
import fr.umontpellier.iut.enchven.CheckWinnerQuery;
import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryCouple;
import fr.umontpellier.iut.queryhelper.QuerySerializer;
import fr.umontpellier.iut.queryhelper.QueryType;
import fr.umontpellier.iut.authclientforge.profile.AuthProfile;
import fr.umontpellier.iut.shared.Bid;
import fr.umontpellier.iut.shared.BidValidator;
import fr.umontpellier.iut.shared.SignedQueryValidator;
import fr.umontpellier.iut.socketwrapper.ConnectionRefresher;
import fr.umontpellier.iut.socketwrapper.exceptions.RefresherClosedException;
import fr.umontpellier.iut.socketwrapper.exceptions.RemoteUnavailableException;
import fr.umontpellier.iut.socketwrapper.exceptions.SocketClosedException;
import fr.umontpellier.iut.socketwrapper.router.EventManager;

import java.io.IOException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Optional;

public class AuctionManager {
    private Auction auction;

    private SignedQueryValidator signedQueryValidator;
    private BidValidator bidValidator;
    private KeyConverterService encryptionKeyConverterService;

    private ConnectionRefresher autorite;
    private EventManager autoriteEventManager;
    private ConnectionRefresher vendeur;
    private EventManager vendeurEventManager;

    private AuthProfile authProfile;
    private BidBuilder bidBuilder;

    private void initializeVendeurEvents() {
        vendeurEventManager.addQueryCouple("checkBids", new QueryCouple<>(CheckBidsQuery.class,
                new CheckBidsQueryHandler(auction, signedQueryValidator, bidValidator, autorite)));

        vendeurEventManager.addQueryCouple("checkWinner", new QueryCouple<>(CheckWinnerQuery.class,
                new CheckWinnerQueryHandler(auction, autorite, signedQueryValidator)));
    }

    private void initializeAutoriteEvents() {
        autoriteEventManager.addQueryCouple("sendKey", new QueryCouple<>(SendKeyQuery.class,
                new SendKeyQueryHandler(auction, signedQueryValidator, encryptionKeyConverterService)));
    }

    public AuctionManager(Auction auction, SignedQueryValidator signedQueryValidator, BidValidator bidValidator,
                          BidBuilder bidBuilder, KeyConverterService encryptionKeyConverterService,
                          ConnectionRefresher autorite,
                          EventManager autoriteEventManager, ConnectionRefresher vendeur,
                          EventManager vendeurEventManager, AuthProfile authProfile) {
        this.auction = auction;
        this.signedQueryValidator =signedQueryValidator;
        this.bidValidator = bidValidator;
        this.encryptionKeyConverterService = encryptionKeyConverterService;

        this.autorite = autorite;
        this.autoriteEventManager = autoriteEventManager;
        initializeAutoriteEvents();

        this.vendeur = vendeur;
        this.vendeurEventManager = vendeurEventManager;
        initializeVendeurEvents();


        this.authProfile = authProfile;
        this.bidBuilder = bidBuilder;
    }

    private void askForKey() throws RemoteUnavailableException, IOException, SocketClosedException, RefresherClosedException {

        KeyQuery keyQuery = new KeyQuery(authProfile.getToken().get());
        QuerySerializer<KeyQuery> serializer = new QuerySerializer<>();
        autorite.getConnection().send(serializer.serialize(keyQuery));
    }

    private void sendBid(Bid bid) throws SocketClosedException, RemoteUnavailableException, IOException, RefresherClosedException {
        BiddingQuery bidQuery = new BiddingQuery(QueryType.SINGLE, QueryCode.SUCCESS, bid);
        QuerySerializer<BiddingQuery> serializer = new QuerySerializer<>();

        vendeur.getConnection().send(serializer.serialize(bidQuery));
    }

    public Optional<LastBid> bid (String price) throws SocketClosedException, RemoteUnavailableException,
            SignatureException, EncryptException, IOException, RefresherClosedException {
        if(auction.hasAutoriteExpired()) askForKey();

        Optional<PublicKey> autoriteKey = auction.getAutoriteKeyWait();

        if(autoriteKey.isEmpty()) return Optional.empty();

        Bid bid = bidBuilder.buildBid(price, autoriteKey.get());
        LastBid lastBid = new LastBid(bid, Integer.parseInt(price));

        auction.setLastBid(lastBid);
        sendBid(bid);

        return Optional.of(lastBid);
    }

    public void subscribeSecondPrice(SecondPriceObserver observer) {
        auction.subscribeSecondPrice(observer);
    }
}
