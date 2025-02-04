package fr.umontpellier.iut.cryptowrapper.keys.rsa;

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

public class RSAKeysManagerTests {

    @Test
    public void RSAKeysManagerShouldNotThrowException() {
        RSAKeysManager rsaKeysManager = new RSAKeysManager("test-key.pub", "test-key.pvt", 4096);
    }

    @Test
    public void RSAKeysManagerShouldThrowExceptionWhenNamesEqual() {
        assertThrows(RuntimeException.class, () -> {
            RSAKeysManager rsaKeysManager = new RSAKeysManager("test-key.pub", "test-key.pub", 4096);
        });
    }

    @Test
    public void RSAKeysManagerShouldThrowExceptionWhenSizeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            RSAKeysManager rsaKeysManager = new RSAKeysManager("test-key.pub", "test-key.pvt", 521);
        });
    }

    @Test
    public void generateKeysShouldReturnRSAValidKeyPair() {
        RSAKeysManager rsaKeysManager = new RSAKeysManager("test-key.pub", "test-key.pvt", 4096);
        KeyPair keyPair = rsaKeysManager.generateKeys();
        assertTrue(keyPair.getPublic() instanceof RSAPublicKey);
        assertTrue(keyPair.getPrivate() instanceof RSAPrivateKey);
    }

    @Test
    public void writeKeyPairShouldWriteFiles() {
        RSAKeysManager rsaKeysManager = new RSAKeysManager("test-key.pub", "test-key.pvt", 4096);
        Path publicKeyPath = Paths.get("test-key.pub");
        Path privateKeyPath = Paths.get("test-key.pvt");
        KeyPair keyPair = rsaKeysManager.generateKeys();
        rsaKeysManager.writeKeyPair(keyPair);
        assertTrue(Files.exists(publicKeyPath) && Files.exists(privateKeyPath));
    }

    @Test
    public void loadPublicKeyShouldReturnValidPublicKey() {
        RSAKeysManager rsaKeysManager = new RSAKeysManager("test-key.pub", "test-key.pvt", 4096);

        KeyPair rsaKeyPair = rsaKeysManager.generateKeys();
        rsaKeysManager.writeKeyPair(rsaKeyPair);
        PublicKey rsaPublicKey = rsaKeysManager.loadPublicKey();

        assertTrue(rsaPublicKey instanceof RSAPublicKey);
        assertFalse(rsaPublicKey instanceof ECPublicKey);
        assertFalse(rsaPublicKey instanceof RSAPrivateKey);
    }

    @Test
    public void loadPrivateKeyShouldReturnValidPublicKey() {
        RSAKeysManager rsaKeysManager = new RSAKeysManager("test-key.pub", "test-key.pvt", 4096);

        KeyPair rsaKeyPair = rsaKeysManager.generateKeys();
        rsaKeysManager.writeKeyPair(rsaKeyPair);
        PrivateKey rsaPrivateKey = rsaKeysManager.loadPrivateKey();

        assertTrue(rsaPrivateKey instanceof RSAPrivateKey);
        assertFalse(rsaPrivateKey instanceof ECPrivateKey);
        assertFalse(rsaPrivateKey instanceof RSAPublicKey);
    }

    @Test
    public void keyPairExistsTestShouldReturnTrueWhenExist() {
        RSAKeysManager rsaKeysManager = new RSAKeysManager("test-key.pub", "test-key.pvt", 4096);
        KeyPair keyPair = rsaKeysManager.generateKeys();
        rsaKeysManager.writeKeyPair(keyPair);
        assertTrue(rsaKeysManager.keyPairExists());
    }

    @Test
    public void keyPairExistsTestShouldReturnFalseWhenNoExist() {
        RSAKeysManager rsaKeysManager = new RSAKeysManager("test-exist-key.pub", "test-exist-key.pvt", 4096);
        assertFalse(rsaKeysManager.keyPairExists());
    }
}