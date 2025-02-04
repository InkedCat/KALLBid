package fr.umontpellier.iut.cryptowrapper.keys.rsa;

import org.junit.jupiter.api.Test;

import java.security.Key;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class RSAKeyStringConverterTests {

    @Test
    public void keyToB64StringShouldReturnValidB64String() {
        RSAKeysManager rsaKeysManager = new RSAKeysManager("test-key.pub", "test-key.pvt", 4096);
        KeyPair keyPair = rsaKeysManager.generateKeys();

        RSAKeyStringConverter rsaKeyStringConverter = new RSAKeyStringConverter();
        String B64PublicKey = rsaKeyStringConverter.keyToB64String(keyPair.getPublic());

        byte[] decodedBytes = Base64.getDecoder().decode(B64PublicKey);
        String reencodedString = Base64.getEncoder().encodeToString(decodedBytes);

        assertEquals(736, B64PublicKey.length());
        assertEquals(reencodedString, B64PublicKey);
    }

    @Test
    public void B64StringToPublicKeyShouldReturnECValidPublicKey() {
        RSAKeyStringConverter rsaKeyStringConverter = new RSAKeyStringConverter();
        String base64RsaPublicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAmaqQI89OPcX94BVd4pBJaKWT9UYBw+wihwaGN6193DHljM7M7AOZwJNJqETnRFtzKUbbe6hm7mDwJZYTE8sTM5NY37s3AXwjUHMQxya3bNnG8Wu7KrDKXbj9lOOKm84G6pXGk5eg7NQMAJOrEZfHyMkAbE86TJLbh32xy6P7me8swFyHZQG2l6vMnYJv5k7B64st1X+lhjiyfadAbyH/IDONqjubNgwQaftVJ3ojlRzOYso/Ly2MMdBkz3dyf9/uJzP0yTPWfHRe2s/d0WIFevNzsYZES5GgI1QhW8Drz3th6rZep4eizenpTdh14WSWQTy3p/DGogQft7b9UD1xxWQduan2wG0OhafGjGlHv86iZbHPX6bVNwTahAeX6Toj5ieikA66ykDXRwYxkq7smAgDXStBknOcsOGHN0kuZQD4j5s3J2+BYbJbILkQyP2ORIKYaNC9MBPj9Vjj/DdeXzKMlF7he5wSadlDmcF2oXxyPihCVU8iGKBq7/QONiCUOcghRMqCbP5mDvRbGITV2EvsF3uD+aYMw2B+TnOSazKrkvzOXYen/SWUaMzcRJx2HJqeG+6msQ6O7xxAuWmGCcSj6YoHQAekCaRBdyS3pUv15BzS1qcTDv6JITvCYpcjha3UtKHAMYpINXlVrW0hLp237bCKYY2vmf2yp8SxECkCAwEAAQ==";
        Key key = null;

        try {
            key = rsaKeyStringConverter.B64StringToPublicKey(base64RsaPublicKey);
        } catch (Exception e) {
            System.err.println(e);
        }

        assertTrue(key instanceof RSAPublicKey);
        assertFalse(key instanceof RSAPrivateKey);
        assertEquals(base64RsaPublicKey, rsaKeyStringConverter.keyToB64String(key));
    }

    @Test
    public void B64StringToPrivateKeyShouldReturnECValidPrivateKey() {
        RSAKeyStringConverter rsaKeyStringConverter = new RSAKeyStringConverter();
        String base64RsaPrivateKey = "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";
        Key key = null;

        try {
            key = rsaKeyStringConverter.B64StringToPrivateKey(base64RsaPrivateKey);
        } catch (Exception e) {
            System.err.println(e);
        }

        assertTrue(key instanceof RSAPrivateKey);
        assertFalse(key instanceof RSAPublicKey);
        assertEquals(base64RsaPrivateKey, rsaKeyStringConverter.keyToB64String(key));
    }
}