package fr.umontpellier.iut.vendeur;

import fr.umontpellier.iut.authclientforge.AuthProfileManager;
import fr.umontpellier.iut.cryptowrapper.keys.AbstractKeyStringConverter;
import fr.umontpellier.iut.cryptowrapper.keys.AbstractKeysManager;
import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.keys.KeyManagerService;
import fr.umontpellier.iut.cryptowrapper.signature.AbstractSignatureHelper;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;
import fr.umontpellier.iut.queryhelper.QueryCouple;
import fr.umontpellier.iut.authclientforge.profile.AuthProfile;
import fr.umontpellier.iut.shared.AuthResult;
import fr.umontpellier.iut.authclientforge.handler.AuthResultQueryHandler;
import fr.umontpellier.iut.shared.SignedQueryValidator;
import fr.umontpellier.iut.socketwrapper.ConnectionRefresher;
import fr.umontpellier.iut.socketwrapper.exceptions.RefresherClosedException;
import fr.umontpellier.iut.socketwrapper.exceptions.RemoteUnavailableException;
import fr.umontpellier.iut.socketwrapper.exceptions.SocketClosedException;
import fr.umontpellier.iut.socketwrapper.router.EventManager;
import fr.umontpellier.iut.socketwrapper.router.QueryRouterFactory;
import fr.umontpellier.iut.socketwrapper.router.QueryServerManager;
import fr.umontpellier.iut.vendeur.auction.Auction;
import fr.umontpellier.iut.vendeur.auction.AuctionManager;
import fr.umontpellier.iut.vendeur.auction.AuctionWinner;
import fr.umontpellier.iut.vendeur.auction.BiddingObserver;
import fr.umontpellier.iut.vendeur.bids.BidCouple;
import fr.umontpellier.iut.vendeur.bids.strategies.AnonymizedStrategy;
import fr.umontpellier.iut.vendeur.bids.strategies.BidListStrategy;
import fr.umontpellier.iut.vendeur.bids.strategies.SimpleStrategy;
import fr.umontpellier.iut.vendeur.builder.CheckBidsQueryBuilder;
import fr.umontpellier.iut.vendeur.builder.CheckWinnerQueryBuilder;
import fr.umontpellier.iut.vendeur.builder.FindWinnerQueryBuilder;

import java.io.IOException;
import java.security.KeyPair;
import java.util.List;
import java.util.Optional;

public class AppVendeur {
    private AuctionManager auctionManager;
    private SignatureService signatureService;
    private KeyConverterService signingKeyConverterService;

    private KeyPair keyPair;
    private ConnectionRefresher autorite;
    private EventManager autoriteEventManager;
    private AuthProfileManager authProfileManager;

    public AppVendeur(AbstractSignatureHelper signatureHelper, AbstractKeyStringConverter signingKeyConverter,
                      AbstractKeysManager signingKeysManager) {
        keyPair = new KeyManagerService(signingKeysManager).getKeyPair();

        signatureService = new SignatureService(keyPair, signatureHelper);

        signingKeyConverterService = new KeyConverterService(signingKeyConverter);

        autoriteEventManager = new EventManager();
        autorite = new ConnectionRefresher("127.0.0.1", 5000,
                new QueryRouterFactory(autoriteEventManager), 5000);

        authProfileManager = new AuthProfileManager(new AuthProfile(), autorite);
        autoriteEventManager.addQueryCouple("authResult",
                new QueryCouple<>(AuthResult.class, new AuthResultQueryHandler(authProfileManager.getProfile())));
    }

    public void startAuction() throws IOException {
        QueryServerManager auctionServer = new QueryServerManager(6000, 5000);
        Auction auction = new Auction(5);

        SignedQueryValidator signedQueryValidator = new SignedQueryValidator(signatureService, signingKeyConverterService);
        CheckBidsQueryBuilder checkBidsQueryBuilder = new CheckBidsQueryBuilder(signatureService, signingKeyConverterService);
        CheckWinnerQueryBuilder checkWinnerQueryBuilder = new CheckWinnerQueryBuilder(signatureService, signingKeyConverterService);
        FindWinnerQueryBuilder findWinnerQueryBuilder = new FindWinnerQueryBuilder(signatureService, signingKeyConverterService);
        BidListStrategy anonymisedStrategy = new AnonymizedStrategy(new SimpleStrategy(), signatureService, signingKeyConverterService);

        auctionManager = new AuctionManager(auctionServer, auction, autorite,
                autoriteEventManager, signedQueryValidator, checkBidsQueryBuilder, checkWinnerQueryBuilder,
                findWinnerQueryBuilder, anonymisedStrategy, authProfileManager.getProfile());
    }

    public List<BidCouple> consultBids() {
        return auctionManager.consultBids();
    }

    public Optional<AuctionWinner> findWinner() throws RefresherClosedException, SocketClosedException, RemoteUnavailableException, IOException {
        return auctionManager.findWinner();
    }

    public void subscribeToBids(BiddingObserver observer) {
        auctionManager.subscribeToBids(observer);
    }

    public AuthProfileManager getAuthProfileManager() {
        return authProfileManager;
    }

    public void cleanRessources() {
        autorite.stop();
        auctionManager.clean();
    }
}