package fr.umontpellier.iut.encherisseur.handler;

import fr.umontpellier.iut.autench.SendKeyQuery;
import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.encherisseur.auction.Auction;
import fr.umontpellier.iut.queryhelper.QueryHandler;
import fr.umontpellier.iut.shared.SignedQueryValidator;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Optional;

public class SendKeyQueryHandler implements QueryHandler<SendKeyQuery> {

    private Auction auction;
    private SignedQueryValidator signedQueryValidator;
    private KeyConverterService encryptionKeyConverterService;

    public SendKeyQueryHandler(Auction auction, SignedQueryValidator signedQueryValidator,
                               KeyConverterService encryptionKeyConverterService) {
        this.auction = auction;
        this.signedQueryValidator = signedQueryValidator;
        this.encryptionKeyConverterService = encryptionKeyConverterService;
    }

    @Override
    public Optional<String> handle(SendKeyQuery query) {

        if(!signedQueryValidator.validate(query)) return Optional.empty();

        try {
            PublicKey publicKey = encryptionKeyConverterService.stringToPublicKey(query.getCipherKey());
            auction.setAutoriteKey(publicKey);
        } catch (GeneralSecurityException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }
}
