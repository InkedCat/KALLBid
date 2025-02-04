package fr.umontpellier.iut.cryptowrapper.signature.ecdsa;

import fr.umontpellier.iut.cryptowrapper.signature.AbstractSignatureHelper;

public class SHA512_ECDSASignatureHelper extends AbstractSignatureHelper {

    public SHA512_ECDSASignatureHelper() {
        super("SHA512withECDSA");
    }
}