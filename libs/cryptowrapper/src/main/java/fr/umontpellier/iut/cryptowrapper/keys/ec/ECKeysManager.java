package fr.umontpellier.iut.cryptowrapper.keys.ec;

import fr.umontpellier.iut.cryptowrapper.keys.AbstractKeysManager;

public class ECKeysManager extends AbstractKeysManager {
    public ECKeysManager(String publicFileName, String privateFileName, int keysSize) {
        super(publicFileName, privateFileName, "EC", keysSize);
        if (keysSize != 256 && keysSize != 384 && keysSize != 521) {
            throw new IllegalArgumentException("la taille " + keysSize + " est invalide (256, 384, 521)");
        }
    }
}