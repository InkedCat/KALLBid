package fr.umontpellier.iut.vendeur;

import fr.umontpellier.iut.cryptowrapper.keys.ec.ECKeyStringConverter;
import fr.umontpellier.iut.cryptowrapper.keys.ec.ECKeysManager;
import fr.umontpellier.iut.cryptowrapper.signature.ecdsa.SHA512_ECDSASignatureHelper;

public class Vendeur {

    public static void main(String[] args) {
        AppVendeur app = new AppVendeur(new SHA512_ECDSASignatureHelper(), new ECKeyStringConverter(),
                new ECKeysManager("signing-key-vendeur.pub", "signing-key-vendeur.pvt", 521));

        AppVendeurGUI.setBusinessLogic(app);

        AppVendeurGUI.main(args);
    }
}
