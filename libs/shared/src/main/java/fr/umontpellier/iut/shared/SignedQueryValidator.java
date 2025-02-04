package fr.umontpellier.iut.shared;

import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;
import fr.umontpellier.iut.queryhelper.SignedQuery;

import java.security.GeneralSecurityException;
import java.security.PublicKey;

public class SignedQueryValidator {
    private SignatureService signatureService;
    private KeyConverterService signingKeyConverterService;

    public SignedQueryValidator(SignatureService signatureService, KeyConverterService signingKeyConverterService) {
        this.signatureService = signatureService;
        this.signingKeyConverterService = signingKeyConverterService;
    }

    private PublicKey convertEncodedKey(String encodedKey) throws GeneralSecurityException {
        return signingKeyConverterService.stringToPublicKey(encodedKey);
    }

    public boolean validate(SignedQuery signedQuery) {
        try {
            PublicKey publicSignKey = convertEncodedKey(signedQuery.getEncodedKey());

            return signatureService.verifySignature(signedQuery.getSignedMessage(), signedQuery.getSignature(), publicSignKey);
        } catch (GeneralSecurityException e) {
            return false;
        }
    }
}
