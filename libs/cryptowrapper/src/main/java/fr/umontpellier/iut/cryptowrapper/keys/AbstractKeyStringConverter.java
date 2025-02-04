package fr.umontpellier.iut.cryptowrapper.keys;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public abstract class AbstractKeyStringConverter {
    private String ALGORITHM;

    public AbstractKeyStringConverter(String ALGORITHM) {
        this.ALGORITHM = ALGORITHM;
    }

    /**
     * Convertit une clé en un chaîne de caractères
     * @param key clé à convertir
     * @return chaîne de caractères correspondant à la clé encodé au format base 64
     */
    public String keyToB64String(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * Convertit une chaîne de caractère en clé publique
     * @param keyString chaîne de caractères correspondant à une clé publique encodée au format base 64
     * @return une clé publique
     * @throws GeneralSecurityException
     */
    public PublicKey B64StringToPublicKey(String keyString) throws GeneralSecurityException {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(spec);
    }

    /**
     * Convertit une chaîne de caractère en clé privée
     * @param keyString chaîne de caractères correspondant à une clé privée encodée au format base 64
     * @return une clé privée
     * @throws GeneralSecurityException
     */
    public PrivateKey B64StringToPrivateKey(String keyString) throws GeneralSecurityException {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(spec);
    }
}