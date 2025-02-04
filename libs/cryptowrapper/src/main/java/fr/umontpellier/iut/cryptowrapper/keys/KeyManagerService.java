package fr.umontpellier.iut.cryptowrapper.keys;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyManagerService {
    private final AbstractKeysManager keysManager;

    public KeyManagerService(AbstractKeysManager keysManager) {
        this.keysManager = keysManager;
    }

    public KeyPair getKeyPair() {
        if(!keysManager.keyPairExists()) {
            KeyPair kp = keysManager.generateKeys();
            keysManager.writeKeyPair(kp);
            return kp;
        }

        PublicKey publicKey = keysManager.loadPublicKey();
        PrivateKey privateKey = keysManager.loadPrivateKey();

        return new KeyPair(publicKey, privateKey);
    }
}
