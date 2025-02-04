package fr.umontpellier.iut.cryptowrapper.signature.ec;

import fr.umontpellier.iut.cryptowrapper.keys.ec.ECKeyStringConverter;
import fr.umontpellier.iut.cryptowrapper.keys.ec.ECKeysManager;
import fr.umontpellier.iut.cryptowrapper.signature.ecdsa.SHA512_ECDSASignatureHelper;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class SHA512_ECDSASignatureHelperTests {

    @Test
    public void signToB64ShouldReturnValidB64Signature() {
        SHA512_ECDSASignatureHelper sha512_ecdsaSignatureHelper = new SHA512_ECDSASignatureHelper();
        ECKeyStringConverter ecKeyStringConverter = new ECKeyStringConverter();
        ECKeysManager ecKeysManager = new ECKeysManager("test-key.pub", "test-key.pvt", 521);

        String message = "Hello world !";

        try {
            KeyPair keyPair = ecKeysManager.generateKeys();
            String signature = sha512_ecdsaSignatureHelper.signToB64(message, keyPair.getPrivate());
            assertTrue(sha512_ecdsaSignatureHelper.checkB64Signature(message, signature, keyPair.getPublic()));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Test
    public void checkB64SignatureShouldReturnTrueWhenSignatureValid() {
        SHA512_ECDSASignatureHelper sha512_ecdsaSignatureHelper = new SHA512_ECDSASignatureHelper();
        ECKeyStringConverter ecKeyStringConverter = new ECKeyStringConverter();

        String stringPublicKey = "MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQAP8bGyJbPiOxvSa0VlRHOcWWl7TtojBCD/uRJakjld0u26okr0CJoftJOsBWgbF5SvBvys6Vke3VWHZYErqiJblQALvBpG6zJVuHnUooBpwOLVylGE4wjR+egSNyqwkYstgqFBYm/lxvrsfJlR7lon6C5m1rz0sWE8hN97IdWnKKdE6A=";
        String message = "Hello world !";
        String signature = "MIGIAkIA6vdJi1/cd55CgtJ/mAf0corUYFpsuvbwlYGGD32Z8noealZa0hJpVFqCk6C4EmCRYMLNknwEFPW3MUEssN+uhUACQgD8VmubvwIKL/NhWzTEeYIyoaxg10v7oI3JYENtCo6dZ9PkoGDQzeCv+VJKVDNxjOrY+8EfL+/Oie5wBWWn7Ex2MQ==";

        try {
            PublicKey publicKey = ecKeyStringConverter.B64StringToPublicKey(stringPublicKey);
            assertTrue(sha512_ecdsaSignatureHelper.checkB64Signature(message, signature, publicKey));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Test
    public void checkB64SignatureShouldReturnFalseWhenSignatureInvalid() {
        SHA512_ECDSASignatureHelper sha512_ecdsaSignatureHelper = new SHA512_ECDSASignatureHelper();
        ECKeyStringConverter ecKeyStringConverter = new ECKeyStringConverter();

        String stringPublicKey = "MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQAP8bGyJbPiOxvSa0VlRHOcWWl7TtojBCD/uRJakjld0u26okr0CJoftJOsBWgbF5SvBvys6Vke3VWHZYErqiJblQALvBpG6zJVuHnUooBpwOLVylGE4wjR+egSNyqwkYstgqFBYm/lxvrsfJlR7lon6C5m1rz0sWE8hN97IdWnKKdE6A=";
        String message = "Hello world !!!";
        String signature = "MIGIAkIA6vdJi1/cd55CgtJ/mAf0corUYFpsuvbwlYGGD32Z8noealZa0hJpVFqCk6C4EmCRYMLNknwEFPW3MUEssN+uhUACQgD8VmubvwIKL/NhWzTEeYIyoaxg10v7oI3JYENtCo6dZ9PkoGDQzeCv+VJKVDNxjOrY+8EfL+/Oie5wBWWn7Ex2MQ==";

        try {
            PublicKey publicKey = ecKeyStringConverter.B64StringToPublicKey(stringPublicKey);
            assertFalse(sha512_ecdsaSignatureHelper.checkB64Signature(message, signature, publicKey));
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}