package fr.umontpellier.iut.autorite.builder;

import fr.umontpellier.iut.autench.SendKeyQuery;
import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.Optional;

public class SendKeyQueryBuilder {
    private KeyPair cipherKeyPair;
    private SignatureService signatureService;
    private KeyConverterService signingKeyConverterService;
    private KeyConverterService encryptionKeyConverterService;

    public SendKeyQueryBuilder(KeyPair cipherKeyPair, SignatureService signatureService,
                               KeyConverterService signingKeyConverterService,
                               KeyConverterService encryptionKeyConverterService) {
        this.cipherKeyPair = cipherKeyPair;
        this.signatureService = signatureService;
        this.signingKeyConverterService = signingKeyConverterService;
        this.encryptionKeyConverterService = encryptionKeyConverterService;

    }

    public Optional<SendKeyQuery> build() {
        try {
            String encodedCipherKey = encryptionKeyConverterService.keyToString(cipherKeyPair.getPublic());
            String encodedSignatureKey = signingKeyConverterService.keyToString(signatureService.getSigningKeys().getPublic());

            return Optional.of(new SendKeyQuery(signatureService.sign(encodedCipherKey), encodedSignatureKey, encodedCipherKey));
        } catch (GeneralSecurityException e) {
            return Optional.empty();
        }
    }
}
