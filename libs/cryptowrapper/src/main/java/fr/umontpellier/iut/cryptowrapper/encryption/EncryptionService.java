package fr.umontpellier.iut.cryptowrapper.encryption;

import fr.umontpellier.iut.cryptowrapper.encryption.exceptions.DecryptException;
import fr.umontpellier.iut.cryptowrapper.encryption.exceptions.EncryptException;

import java.security.PrivateKey;
import java.security.PublicKey;

public class EncryptionService {
    private AbstractEncryptionHelper encryptionHelper;

    public EncryptionService(AbstractEncryptionHelper encryptionHelper) {
        this.encryptionHelper = encryptionHelper;
    }

    public String encrypt(String message, PublicKey encryptingKey) throws EncryptException {
        return encryptionHelper.B64encryptData(message, encryptingKey);
    }

    public String decrypt(String message, PrivateKey decryptingKey) throws DecryptException {
        return encryptionHelper.B64decryptData(message, decryptingKey);
    }
}
