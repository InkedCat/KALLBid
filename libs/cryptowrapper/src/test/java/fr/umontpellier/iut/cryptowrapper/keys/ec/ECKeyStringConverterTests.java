package fr.umontpellier.iut.cryptowrapper.keys.ec;

import org.junit.jupiter.api.Test;

import java.security.Key;
import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class ECKeyStringConverterTests {

    @Test
    public void keyToB64StringShouldReturnValidB64String() {
        ECKeysManager ecKeysManager = new ECKeysManager("test-key.pub", "test-key.pvt", 521);
        KeyPair keyPair = ecKeysManager.generateKeys();

        ECKeyStringConverter ecKeyStringConverter = new ECKeyStringConverter();
        String base64EcPublicKey = ecKeyStringConverter.keyToB64String(keyPair.getPublic());

        byte[] decodedBytes = Base64.getDecoder().decode(base64EcPublicKey);
        String reencodedString = Base64.getEncoder().encodeToString(decodedBytes);

        assertEquals(212, base64EcPublicKey.length());
        assertEquals(reencodedString, base64EcPublicKey);
    }

    @Test
    public void B64StringToPublicKeyShouldReturnECValidPublicKey() {
        ECKeyStringConverter ecKeyStringConverter = new ECKeyStringConverter();
        String base64EcPublicKey = "MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQAvRDLkJFGrLwDniBhpIJC0IP0uuHE1CzNP72TCtS28ANKotrTFB40OWcjLmGVYZKs7Q1NffYHdEtLsj4wtJk43vgBY/Z3wrzJCABtznYdPp9Arxy4cYMxhaQwKJWwD5xEDEuqayPdEIktBcjjqQpZ1Iw2KMLTjZhTQmqG/LSD8UcDDIM=";
        Key key = null;

        try {
            key = ecKeyStringConverter.B64StringToPublicKey(base64EcPublicKey);
        } catch (Exception e) {
            System.err.println(e);
        }

        assertTrue(key instanceof ECPublicKey);
        assertFalse(key instanceof ECPrivateKey);
        assertEquals(base64EcPublicKey, ecKeyStringConverter.keyToB64String(key));
    }

    @Test
    public void B64StringToPrivateKeyShouldReturnECValidPrivateKey() {
        ECKeyStringConverter ecKeyStringConverter = new ECKeyStringConverter();
        String base64EcPrivateKey = "MGACAQAwEAYHKoZIzj0CAQYFK4EEACMESTBHAgEBBEIBcaL5YJEVma9SFQouNbxzI7e3HMAfnmjcIJzPuniMhaEwcz2ScG5cYYsX9DThoHg3+lrk5hLx7ZEo/7EzfF/hEUg=";
        Key key = null;

        try {
            key = ecKeyStringConverter.B64StringToPrivateKey(base64EcPrivateKey);
        } catch (Exception e) {
            System.err.println(e);
        }

        assertTrue(key instanceof ECPrivateKey);
        assertFalse(key instanceof ECPublicKey);
        assertEquals(base64EcPrivateKey, ecKeyStringConverter.keyToB64String(key));
    }
}