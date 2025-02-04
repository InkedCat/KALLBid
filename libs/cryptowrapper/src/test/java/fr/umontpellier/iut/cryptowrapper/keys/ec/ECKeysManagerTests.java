package fr.umontpellier.iut.cryptowrapper.keys.ec;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class ECKeysManagerTests {

    @Test
    public void ECKeysManagerShouldNotThrowException() {
        ECKeysManager ecKeysManager = new ECKeysManager("test-key.pub", "test-key.pvt", 521);
    }

    @Test
    public void ECKeysManagerShouldThrowExceptionWhenNamesEqual() {
        assertThrows(RuntimeException.class, () -> {
            ECKeysManager ecKeysManager = new ECKeysManager("test-key.pub", "test-key.pub", 4096);
        });
    }

    @Test
    public void ECKeysManagerShouldThrowExceptionWhenSizeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            ECKeysManager ecKeysManager = new ECKeysManager("test-key.pub", "test-key.pvt", 4096);
        });
    }

    @Test
    public void generateKeysShouldReturnECValidKeyPair() {
        ECKeysManager ecKeysManager = new ECKeysManager("test-key.pub", "test-key.pvt", 521);
        KeyPair keyPair = ecKeysManager.generateKeys();
        assertTrue(keyPair.getPublic() instanceof ECPublicKey);
        assertTrue(keyPair.getPrivate() instanceof ECPrivateKey);
    }

    @Test
    public void writeKeyPairShouldWriteFiles() {
        ECKeysManager ecKeysManager = new ECKeysManager("test-key.pub", "test-key.pvt", 521);
        Path publicKeyPath = Paths.get("test-key.pub");
        Path privateKeyPath = Paths.get("test-key.pvt");
        KeyPair keyPair = ecKeysManager.generateKeys();
        ecKeysManager.writeKeyPair(keyPair);
        assertTrue(Files.exists(publicKeyPath) && Files.exists(privateKeyPath));
    }

    @Test
    public void loadPublicKeyShouldReturnValidPublicKey() {
        ECKeysManager ecKeysManager = new ECKeysManager("test-key.pub", "test-key.pvt", 521);

        KeyPair ecKeyPair = ecKeysManager.generateKeys();
        ecKeysManager.writeKeyPair(ecKeyPair);
        PublicKey ecPublicKey = ecKeysManager.loadPublicKey();

        assertTrue(ecPublicKey instanceof  ECPublicKey);
        assertFalse(ecPublicKey instanceof RSAPublicKey);
        assertFalse(ecPublicKey instanceof ECPrivateKey);
    }

    @Test
    public void loadPrivateKeyShouldReturnValidPublicKey() {
        ECKeysManager ecKeysManager = new ECKeysManager("test-key.pub", "test-key.pvt", 521);

        KeyPair ecKeyPair = ecKeysManager.generateKeys();
        ecKeysManager.writeKeyPair(ecKeyPair);
        PrivateKey ecPrivateKey = ecKeysManager.loadPrivateKey();

        assertTrue(ecPrivateKey instanceof  ECPrivateKey);
        assertFalse(ecPrivateKey instanceof RSAPrivateKey);
        assertFalse(ecPrivateKey instanceof ECPublicKey);
    }

    @Test
    public void keyPairExistsTestShouldReturnTrueWhenExist() {
        ECKeysManager ecKeysManager = new ECKeysManager("test-key.pub", "test-key.pvt", 521);
        KeyPair keyPair = ecKeysManager.generateKeys();
        ecKeysManager.writeKeyPair(keyPair);
        assertTrue(ecKeysManager.keyPairExists());
    }

    @Test
    public void keyPairExistsTestShouldReturnFalseWhenNoExist() {
        ECKeysManager ecKeysManager = new ECKeysManager("test-exist-key.pub", "test-exist-key.pvt", 521);
        assertFalse(ecKeysManager.keyPairExists());
    }
}