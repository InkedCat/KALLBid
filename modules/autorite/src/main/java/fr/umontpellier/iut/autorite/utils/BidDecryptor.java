package fr.umontpellier.iut.autorite.utils;

import fr.umontpellier.iut.cryptowrapper.encryption.EncryptionService;
import fr.umontpellier.iut.cryptowrapper.encryption.exceptions.DecryptException;

import java.security.KeyPair;

public class BidDecryptor {
    private KeyPair cipherKeyPair;
    private EncryptionService encryptionService;

    public BidDecryptor(KeyPair cipherKeyPair, EncryptionService encryptionService) {
        this.cipherKeyPair = cipherKeyPair;
        this.encryptionService = encryptionService;
    }

    public int decrypt(String encrypted) throws DecryptException {
        return Integer.parseInt(
                encryptionService.decrypt(encrypted, cipherKeyPair.getPrivate()));
    }
}
