package fr.umontpellier.iut.autorite.builder;

import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;
import fr.umontpellier.iut.shared.Winner;
import fr.umontpellier.iut.venaut.SendWinnerQuery;

import java.security.GeneralSecurityException;
import java.util.Optional;

public class SendWinnerQueryBuilder {
    private SignatureService signatureService;
    private KeyConverterService signingKeyConverterService;

    public SendWinnerQueryBuilder(SignatureService signatureService, KeyConverterService signingKeyConverterService) {
        this.signatureService = signatureService;
        this.signingKeyConverterService = signingKeyConverterService;
    }

    public Optional<SendWinnerQuery> build(Winner winner) {
        try {
            String signature = signatureService.sign(winner.getMessage());
            String encodedKey = signingKeyConverterService.keyToString(signatureService.getSigningKeys().getPublic());

            return Optional.of(new SendWinnerQuery(signature, encodedKey, winner.getWinnerPrice(), winner.getSecondPrice()));
        } catch (GeneralSecurityException e) {
            return Optional.empty();
        }
    }
}
