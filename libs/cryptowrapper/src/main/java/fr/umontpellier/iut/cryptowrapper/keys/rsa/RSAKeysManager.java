package fr.umontpellier.iut.cryptowrapper.keys.rsa;

import fr.umontpellier.iut.cryptowrapper.keys.AbstractKeysManager;

public class RSAKeysManager extends AbstractKeysManager {
    public RSAKeysManager(String publicFileName, String privateFileName, int keysSize) {
        super(publicFileName, privateFileName, "RSA", keysSize);
        if (keysSize != 512 && keysSize != 1024 && keysSize != 2048 && keysSize != 4096) {
            throw new IllegalArgumentException("la taille " + keysSize + " est invalide (512, 1024, 2048, 4096)");
        }
    }
}