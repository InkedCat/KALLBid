package fr.umontpellier.iut.cryptowrapper.signature;

import java.security.*;
import java.util.Base64;

public abstract class AbstractSignatureHelper {

    private String ALGORITHM;

    public AbstractSignatureHelper(String algorithm) {
        ALGORITHM = algorithm;
    }

    /**
     * Signe un message à partir d'une clé privée
     * @param message chaîne de caractères correspondant au message à signer
     * @param key clé privée de signature
     * @return chaîne de caractère correspondant à la signature du message encodé au format base 64
     * @throws SignatureException
     */
    public String signToB64(String message, PrivateKey key) throws SignatureException {
        try {

            Signature signatureObject = Signature.getInstance(ALGORITHM);
            signatureObject.initSign(key);
            signatureObject.update(message.getBytes());

            byte[] signatureBytes = signatureObject.sign();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (NullPointerException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new SignatureException();
        }
    }

    /**
     * Vérifie si la signature d'un message est correcte à partir de la clé publique de signature fournie
     * @param message chaîne de caractères correspondant au message en clair
     * @param signature chaîne de caractères correspondant à la signature encodé au format base 64
     * @param publicKey clé publique associée à la signature
     * @return true ou false en fonction de si la signature est juste
     */
    public boolean checkB64Signature(String message, String signature, PublicKey publicKey) {
        try {
            Signature signatureObject = Signature.getInstance(ALGORITHM);
            signatureObject.initVerify(publicKey);
            signatureObject.update(message.getBytes());

            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return signatureObject.verify(signatureBytes);
        } catch (IllegalArgumentException | NullPointerException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            return false;
        }
    }
}