package fr.umontpellier.iut.shared;

import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;

import java.security.GeneralSecurityException;
import java.security.PublicKey;

public class BidValidator {
    private SignatureService signatureService;
    private KeyConverterService signingKeyConverterService;

    public BidValidator(SignatureService signatureService, KeyConverterService signingKeyConverterService) {
        this.signatureService = signatureService;
        this.signingKeyConverterService = signingKeyConverterService;
    }

    private PublicKey convertEncodedKey(String encodedKey) throws GeneralSecurityException {
        return signingKeyConverterService.stringToPublicKey(encodedKey);
    }

    public boolean validateBid(Bid bid) {
        try {
            PublicKey publicSignKey = convertEncodedKey(bid.encodedKey());

            return signatureService.verifySignature(bid.price(), bid.signature(), publicSignKey);
        } catch (GeneralSecurityException e) {
            return false;
        }
    }
}
