package fr.umontpellier.iut.cryptowrapper.signature;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.SignatureException;

public class SignatureService {
    private KeyPair signingKeys;
    private AbstractSignatureHelper signatureHelper;

    public SignatureService(KeyPair signingKeys, AbstractSignatureHelper signatureHelper) {
        this.signingKeys = signingKeys;
        this.signatureHelper = signatureHelper;
    }

    public String sign(String message) throws SignatureException {
        return signatureHelper.signToB64(message, signingKeys.getPrivate());
    }

    public boolean verifySignature(String message, String signature, PublicKey publicKey) {
        return signatureHelper.checkB64Signature(message, signature, publicKey);
    }

    public KeyPair getSigningKeys() {
        return signingKeys;
    }
}
