package fr.umontpellier.iut.cryptowrapper.signature.rsa;

import fr.umontpellier.iut.cryptowrapper.signature.AbstractSignatureHelper;

public class SHA256_RSASignatureHelper extends AbstractSignatureHelper {

    public SHA256_RSASignatureHelper() {
        super("SHA256withRSA");
    }
}