package fr.umontpellier.iut.encherisseur;

import fr.umontpellier.iut.authclientforge.AuthProfileManager;
import fr.umontpellier.iut.cryptowrapper.encryption.AbstractEncryptionHelper;
import fr.umontpellier.iut.cryptowrapper.encryption.EncryptionService;
import fr.umontpellier.iut.cryptowrapper.encryption.exceptions.EncryptException;
import fr.umontpellier.iut.cryptowrapper.keys.AbstractKeyStringConverter;
import fr.umontpellier.iut.cryptowrapper.keys.AbstractKeysManager;
import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.keys.KeyManagerService;
import fr.umontpellier.iut.cryptowrapper.signature.AbstractSignatureHelper;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;
import fr.umontpellier.iut.encherisseur.auction.Auction;
import fr.umontpellier.iut.encherisseur.auction.AuctionManager;
import fr.umontpellier.iut.encherisseur.auction.SecondPriceObserver;
import fr.umontpellier.iut.encherisseur.bidUtils.BidBuilder;
import fr.umontpellier.iut.encherisseur.bidUtils.KeyHolder;
import fr.umontpellier.iut.encherisseur.bidUtils.LastBid;
import fr.umontpellier.iut.queryhelper.QueryCouple;
import fr.umontpellier.iut.authclientforge.profile.AuthProfile;
import fr.umontpellier.iut.shared.AuthResult;
import fr.umontpellier.iut.authclientforge.handler.AuthResultQueryHandler;
import fr.umontpellier.iut.shared.BidValidator;
import fr.umontpellier.iut.shared.SignedQueryValidator;
import fr.umontpellier.iut.socketwrapper.ConnectionRefresher;
import fr.umontpellier.iut.socketwrapper.exceptions.RefresherClosedException;
import fr.umontpellier.iut.socketwrapper.exceptions.RemoteUnavailableException;
import fr.umontpellier.iut.socketwrapper.exceptions.SocketClosedException;
import fr.umontpellier.iut.socketwrapper.router.EventManager;
import fr.umontpellier.iut.socketwrapper.router.QueryRouterFactory;

import java.io.IOException;
import java.security.KeyPair;
import java.security.SignatureException;
import java.util.Optional;

public class AppEncherisseur{
    private AuctionManager auctionManager;
    private KeyPair signingKeyPair;
    private SignatureService signatureService;
    private KeyConverterService signingKeyConverterService;
    private KeyConverterService encryptionKeyConverterService;
    private EncryptionService encryptionService;
    private ConnectionRefresher autorite;
    private EventManager autoriteEventManager;
    private AuthProfileManager authProfileManager;

    public AppEncherisseur(AbstractSignatureHelper signatureHelper, AbstractKeyStringConverter signingKeyConverter,
                           AbstractKeyStringConverter encryptionKeyConverter, AbstractEncryptionHelper encryptionHelper,
                           AbstractKeysManager signingKeysManager) {
        signingKeyPair = new KeyManagerService(signingKeysManager).getKeyPair();

        signatureService = new SignatureService(signingKeyPair, signatureHelper);
        signingKeyConverterService = new KeyConverterService(signingKeyConverter);
        encryptionKeyConverterService = new KeyConverterService(encryptionKeyConverter);
        encryptionService = new EncryptionService(encryptionHelper);

        autoriteEventManager = new EventManager();
        autorite = new ConnectionRefresher("127.0.0.1", 5000,
                new QueryRouterFactory(autoriteEventManager), 5000);

        authProfileManager = new AuthProfileManager(new AuthProfile(), autorite);
        autoriteEventManager.addQueryCouple("authResult",
                new QueryCouple<>(AuthResult.class, new AuthResultQueryHandler(authProfileManager.getProfile())));
        createAuction();
    }

    private void createAuction() {
        Auction auction = new Auction(new KeyHolder<>(15, 15));

        EventManager vendeurEventManager = new EventManager();
        ConnectionRefresher vendeur = new ConnectionRefresher("127.0.0.1", 6000,
                new QueryRouterFactory(vendeurEventManager), 5000);

        SignedQueryValidator signedQueryValidator = new SignedQueryValidator(signatureService, signingKeyConverterService);
        BidValidator bidValidator = new BidValidator(signatureService, signingKeyConverterService);
        BidBuilder bidBuilder = new BidBuilder(signatureService, signingKeyConverterService, encryptionService);


        auctionManager = new AuctionManager(auction, signedQueryValidator, bidValidator, bidBuilder, encryptionKeyConverterService, autorite,
                autoriteEventManager, vendeur, vendeurEventManager, authProfileManager.getProfile());
    }

    public Optional<LastBid> bid(String price) throws SocketClosedException, RemoteUnavailableException,
            SignatureException, IOException, EncryptException, RefresherClosedException {
        if(!authProfileManager.isAuthenticated()) {
            return Optional.empty();
        }

        return auctionManager.bid(price);
    }

    public void subscribeSecondPrice(SecondPriceObserver observer) {
        auctionManager.subscribeSecondPrice(observer);
    }

    public AuthProfileManager getAuthProfileManager() {
        return authProfileManager;
    }

    public void cleanRessources() {
    }
}
