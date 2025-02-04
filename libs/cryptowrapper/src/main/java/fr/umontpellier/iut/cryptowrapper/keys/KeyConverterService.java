package fr.umontpellier.iut.cryptowrapper.keys;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyConverterService {
    private AbstractKeyStringConverter keyStringConverter;

    public KeyConverterService(AbstractKeyStringConverter keyStringConverter) {
        this.keyStringConverter = keyStringConverter;
    }

    public String keyToString(Key key) {
        return keyStringConverter.keyToB64String(key);
    }

    public PrivateKey stringToPrivateKey(String key) throws GeneralSecurityException {
        return keyStringConverter.B64StringToPrivateKey(key);
    }

    public PublicKey stringToPublicKey(String key) throws GeneralSecurityException {
        return keyStringConverter.B64StringToPublicKey(key);
    }
}
