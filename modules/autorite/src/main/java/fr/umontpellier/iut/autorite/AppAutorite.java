package fr.umontpellier.iut.autorite;

import fr.umontpellier.iut.autench.KeyQuery;
import fr.umontpellier.iut.autench.MaliciousAuctionQuery;
import fr.umontpellier.iut.authserverforge.TokensManager;
import fr.umontpellier.iut.autorite.builder.SendKeyQueryBuilder;
import fr.umontpellier.iut.autorite.builder.SendWinnerQueryBuilder;
import fr.umontpellier.iut.autorite.handler.AuthQueryHandler;
import fr.umontpellier.iut.autorite.handler.FindWinnerQueryHandler;
import fr.umontpellier.iut.autorite.handler.KeyQueryHandler;
import fr.umontpellier.iut.autorite.handler.MaliciousAuctionQueryHandler;
import fr.umontpellier.iut.autorite.utils.BidDecryptor;
import fr.umontpellier.iut.autorite.utils.BidListValidator;
import fr.umontpellier.iut.autorite.utils.WinnerFinder;
import fr.umontpellier.iut.cryptowrapper.encryption.AbstractEncryptionHelper;
import fr.umontpellier.iut.cryptowrapper.encryption.EncryptionService;
import fr.umontpellier.iut.cryptowrapper.keys.AbstractKeyStringConverter;
import fr.umontpellier.iut.cryptowrapper.keys.AbstractKeysManager;
import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.keys.KeyManagerService;
import fr.umontpellier.iut.cryptowrapper.signature.AbstractSignatureHelper;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;
import fr.umontpellier.iut.queryhelper.QueryCouple;
import fr.umontpellier.iut.shared.AuthQuery;
import fr.umontpellier.iut.shared.BidValidator;
import fr.umontpellier.iut.shared.SignedQueryValidator;
import fr.umontpellier.iut.socketwrapper.ServerSocketWrapper;
import fr.umontpellier.iut.authserverforge.CredentialsManager;
import fr.umontpellier.iut.socketwrapper.router.EventManager;
import fr.umontpellier.iut.socketwrapper.router.QueryRouterFactory;
import fr.umontpellier.iut.venaut.FindWinnerQuery;

import java.io.IOException;
import java.security.KeyPair;
import java.util.concurrent.atomic.AtomicBoolean;

public class AppAutorite {
    private AtomicBoolean isMalicious;

    private final KeyPair cipherKeyPair;
    private final KeyPair signingKeyPair;
    private SignatureService signatureService;
    private KeyConverterService signingKeyConverterService;
    private KeyConverterService encryptionKeyConverterService;
    private EncryptionService encryptionService;
    private SignedQueryValidator signedQueryValidator;
    private BidListValidator bidListValidator;
    private WinnerFinder winnerFinder;
    private final CredentialsManager credentialsManager;
    private final TokensManager tokensManager;

    private ServerSocketWrapper serverSocketWrapper;
    private EventManager eventManager;
    private boolean auctionStarted = false;

    private void initializeAuthEvents() {
        eventManager.addQueryCouple("auth", new QueryCouple<>(AuthQuery.class, new AuthQueryHandler(credentialsManager, tokensManager)));
    }

    public AppAutorite(AbstractSignatureHelper signatureHelper, AbstractKeyStringConverter signingKeyConverter,
                       AbstractKeyStringConverter encryptionKeyConverter, AbstractEncryptionHelper encryptionHelper,
                       AbstractKeysManager signingKeysManager, AbstractKeysManager encryptionKeysManager) {
        isMalicious = new AtomicBoolean(false);

        signingKeyPair = new KeyManagerService(signingKeysManager).getKeyPair();
        cipherKeyPair = new KeyManagerService(encryptionKeysManager).getKeyPair();

        signatureService = new SignatureService(signingKeyPair, signatureHelper);
        signingKeyConverterService = new KeyConverterService(signingKeyConverter);
        encryptionKeyConverterService = new KeyConverterService(encryptionKeyConverter);
        encryptionService = new EncryptionService(encryptionHelper);

        signedQueryValidator = new SignedQueryValidator(signatureService, signingKeyConverterService);
        bidListValidator = new BidListValidator(new BidValidator(signatureService, signingKeyConverterService));
        winnerFinder = new WinnerFinder(new BidDecryptor(cipherKeyPair, encryptionService));

        credentialsManager = new CredentialsManager();
        tokensManager = new TokensManager();

        eventManager = new EventManager();
        initializeAuthEvents();

        try {
            serverSocketWrapper = new ServerSocketWrapper(5000, new QueryRouterFactory(eventManager), 5000);
        } catch (IOException e) {
            throw new RuntimeException("Ressources occupées, impossible de lancer l'application.");
        }
    }

    public void startAuction() {
        eventManager.addQueryCouple("findWinner", new QueryCouple<>(FindWinnerQuery.class,
                new FindWinnerQueryHandler(signedQueryValidator, bidListValidator,
                        new SendWinnerQueryBuilder(signatureService, signingKeyConverterService), winnerFinder,
                        tokensManager, isMalicious)));

        eventManager.addQueryCouple("key", new QueryCouple<>(KeyQuery.class,
                new KeyQueryHandler(tokensManager, new SendKeyQueryBuilder(cipherKeyPair,  signatureService,
                        signingKeyConverterService, encryptionKeyConverterService))));

        eventManager.addQueryCouple("maliciousAuction", new QueryCouple<>(MaliciousAuctionQuery.class,
                new MaliciousAuctionQueryHandler(isMalicious)));
        auctionStarted = true;
    }

    public void stopAuction() {
        eventManager.removeQueryCouple("findWinner");
        eventManager.removeQueryCouple("key");
        eventManager.removeQueryCouple("maliciousAuction");
        auctionStarted = false;
    }

    public boolean getAuctionState() {
        return auctionStarted;
    }

    public void cleanRessources() {
        if(serverSocketWrapper != null) serverSocketWrapper.close();
    }
}
