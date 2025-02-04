package fr.umontpellier.iut.cryptowrapper.encryption.rsa;

import fr.umontpellier.iut.cryptowrapper.keys.rsa.RSAKeyStringConverter;
import fr.umontpellier.iut.cryptowrapper.keys.rsa.RSAKeysManager;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class RSAEncryptionHelperTests {

    @Test
    public void B64decryptDataShouldReturnClearString() {
        RSAEncryptionHelper rsaEncryptionHelper = new RSAEncryptionHelper();
        RSAKeyStringConverter rsaKeyStringConverter = new RSAKeyStringConverter();

        String stringPrivateKey = "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";
        String encryptedData = "V8azt4+cGYsVv3HMB6h0TeLwXJp6fhY1A2GUPCqSequCfNmqoLwWPf2UztFds16qdRjluhgc23Ykj6q4ExWcWgtoMHbCkcU7P9ILGQ5/tbNPexnKFgozrmK/OB2aqvzrwolPFR6jETuhME5V2ss2mSm2HazA75l12p3KVqoe47Sygz8/oNttrRr0VMqlx0u6JgLJLQg0fzQi+NLn9ZRcJnUs9ZmGujeO9vVfmUILiE2nfvD+ufnWcdCT5P6PbXX661+P7rW85ivmnnH3De+Ro/fgmpZfjH5hdmznqOAWY8FQh8ey+fXrOH5GRGbp41dGgr/oq1rVpKDwaS8HEfg5Z7qhD9HyERZwg9eeeDiVGtGBMV7G8PTMOo343uEyW+HeiQmkA10H7uf7Ad2Na6cj/xIlT7NbcMZYmDTXKlidTKscgfIY7jnKpfaegObnXvoSkl5nEw8h523dyN52LWwMi5+fkmJn+Q10EroF5cVNAiUYeeK6LSn8/MEYKWfiVn5T94tdoDQCcgA/9jh0v1Vk05fClXxQtx7jIP9KK//1sgCO+/DQLOoLtH3GDs/oQk1bmZhUVMBNBomzm4nx9F6xVnBqLvbIEDSCu2aLWFu14IBU/peZLQqOTW/fmwSxdHQlNptBK+pFLw1k1VTRzCkLDZCZedtaKg7em1qXNtoEkxI=";
        String message = "Hello world !";

        try {
            PrivateKey privateKey = rsaKeyStringConverter.B64StringToPrivateKey(stringPrivateKey);
            String data = rsaEncryptionHelper.B64decryptData(encryptedData, privateKey);

            assertEquals(message, data);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Test
    public void B64encryptDataShouldReturnEncryptedString() {
        RSAEncryptionHelper rsaEncryptionHelper = new RSAEncryptionHelper();
        RSAKeysManager rsaKeysManager = new RSAKeysManager("test-key.pub", "test-key.pvt", 4096);
        RSAKeyStringConverter rsaKeyStringConverter = new RSAKeyStringConverter();

        String data = "Hello world !";
        KeyPair keyPair = rsaKeysManager.generateKeys();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        try {
            String encryptedData = rsaEncryptionHelper.B64encryptData(data, publicKey);
            String decryptedData = rsaEncryptionHelper.B64decryptData(encryptedData, privateKey);

            assertEquals(data, decryptedData);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}