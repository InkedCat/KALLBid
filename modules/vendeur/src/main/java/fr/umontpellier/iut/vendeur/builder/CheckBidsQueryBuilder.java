package fr.umontpellier.iut.vendeur.builder;

import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;
import fr.umontpellier.iut.enchven.CheckBidsQuery;
import fr.umontpellier.iut.shared.Bid;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

public class CheckBidsQueryBuilder {
    private final SignatureService signatureService;
    private final KeyConverterService signingKeyConverterService;

    public CheckBidsQueryBuilder(SignatureService signatureService, KeyConverterService signingKeyConverterService) {
        this.signatureService = signatureService;
        this.signingKeyConverterService = signingKeyConverterService;
    }

    public Optional<CheckBidsQuery> build(List<Bid> bids) {
        try {
            String signature = signatureService.sign(bids.toString());
            String encodedSignatureKey = signingKeyConverterService.keyToString(signatureService.getSigningKeys().getPublic());

            return Optional.of(new CheckBidsQuery(signature, encodedSignatureKey, bids));
        } catch (GeneralSecurityException e) {
            return Optional.empty();
        }
    }
}
