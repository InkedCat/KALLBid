package fr.umontpellier.iut.vendeur.builder;

import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;
import fr.umontpellier.iut.enchven.CheckWinnerQuery;

import java.security.GeneralSecurityException;
import java.util.Optional;

public class CheckWinnerQueryBuilder {
    private SignatureService signatureService;
    private KeyConverterService signingKeyConverterService;

    public CheckWinnerQueryBuilder(SignatureService signatureService, KeyConverterService signingKeyConverterService) {
        this.signatureService = signatureService;
        this.signingKeyConverterService = signingKeyConverterService;
    }

    public Optional<CheckWinnerQuery> build(int winnerPrice) {

        try {
            String signature = signatureService.sign(winnerPrice + "");
            String encodedSignatureKey = signingKeyConverterService.keyToString(signatureService.getSigningKeys().getPublic());

            return Optional.of(new CheckWinnerQuery(signature, encodedSignatureKey, winnerPrice));
        } catch (GeneralSecurityException e) {
            return Optional.empty();
        }
    }
}
