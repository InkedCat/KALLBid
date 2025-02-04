package fr.umontpellier.iut.autorite;

import fr.umontpellier.iut.cryptowrapper.encryption.rsa.RSAEncryptionHelper;
import fr.umontpellier.iut.cryptowrapper.keys.ec.ECKeyStringConverter;
import fr.umontpellier.iut.cryptowrapper.keys.ec.ECKeysManager;
import fr.umontpellier.iut.cryptowrapper.keys.rsa.RSAKeyStringConverter;
import fr.umontpellier.iut.cryptowrapper.keys.rsa.RSAKeysManager;
import fr.umontpellier.iut.cryptowrapper.signature.ecdsa.SHA512_ECDSASignatureHelper;

public class Autorite
{

    public static void main(String[] args) {
        AppAutorite appAutorite = new AppAutorite(new SHA512_ECDSASignatureHelper(), new ECKeyStringConverter(),
                new RSAKeyStringConverter(), new RSAEncryptionHelper(),
                new ECKeysManager("signing-key-autorite.pub", "signing-key-autorite.pvt", 521),
                new RSAKeysManager("encryption-key-autorite.pub", "encryption-key-autorite.pvt", 4096));

        boolean cli = false;

        for(String arg : args) {
            if (arg.equals("--cli")) {
                cli = true;
            }
        }

        if(cli) {
            AppAutoriteCLI appAutoriteCLI = new AppAutoriteCLI(appAutorite);
            appAutoriteCLI.run();
        } else {
            AppAutoriteGUI.setBusinessLogic(appAutorite);

            AppAutoriteGUI.main(args);
        }
    }
}