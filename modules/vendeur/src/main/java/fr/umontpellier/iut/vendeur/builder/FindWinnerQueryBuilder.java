package fr.umontpellier.iut.vendeur.builder;

import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;
import fr.umontpellier.iut.shared.Bid;
import fr.umontpellier.iut.venaut.FindWinnerQuery;

import java.util.List;
import java.util.Optional;

public class FindWinnerQueryBuilder {
    private final SignatureService signatureService;
    private final KeyConverterService signingKeyConverterService;

    public FindWinnerQueryBuilder(SignatureService signatureService, KeyConverterService signingKeyConverterService) {
        this.signatureService = signatureService;
        this.signingKeyConverterService = signingKeyConverterService;
    }

    public Optional<FindWinnerQuery> build(List<Bid> bids, String token) {
        try {
            String signature = signatureService.sign(bids.toString());
            String encodedSignatureKey = signingKeyConverterService.keyToString(signatureService.getSigningKeys().getPublic());

            return Optional.of(new FindWinnerQuery(signature, encodedSignatureKey, bids, token));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
