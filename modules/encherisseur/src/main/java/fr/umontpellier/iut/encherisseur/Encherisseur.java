package fr.umontpellier.iut.encherisseur;

import fr.umontpellier.iut.cryptowrapper.encryption.rsa.RSAEncryptionHelper;
import fr.umontpellier.iut.cryptowrapper.keys.ec.ECKeyStringConverter;
import fr.umontpellier.iut.cryptowrapper.keys.ec.ECKeysManager;
import fr.umontpellier.iut.cryptowrapper.keys.rsa.RSAKeyStringConverter;
import fr.umontpellier.iut.cryptowrapper.signature.ecdsa.SHA512_ECDSASignatureHelper;

public class Encherisseur {
    public static void main(String[] args) {
        AppEncherisseur app = new AppEncherisseur(new SHA512_ECDSASignatureHelper(), new ECKeyStringConverter(),
                new RSAKeyStringConverter(), new RSAEncryptionHelper(),
                new ECKeysManager("signing-key-encherisseur.pub", "signing-key-encherisseur.pvt", 521));

        AppEncherisseurGUI.setBusinessLogic(app);

        AppEncherisseurGUI.main(args);
    }
}