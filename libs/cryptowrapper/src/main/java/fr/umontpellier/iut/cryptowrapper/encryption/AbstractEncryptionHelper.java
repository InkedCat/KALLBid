package fr.umontpellier.iut.cryptowrapper.encryption;

import fr.umontpellier.iut.cryptowrapper.encryption.exceptions.DecryptException;
import fr.umontpellier.iut.cryptowrapper.encryption.exceptions.EncryptException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public abstract class AbstractEncryptionHelper {
    private String ALGORITHM;

    public AbstractEncryptionHelper(String algorithm) {
        ALGORITHM = algorithm;
    }

    /**
     * Chiffre une chaîne de caractère à partir d'une clé publique
     * @param data chaîne de caractère à chiffrer
     * @param key clé publique utilisée pour le chiffrement
     * @return une chaîne de caractère correspondant au chiffrement en base 64
     */
    public String B64encryptData(String data, PublicKey key) throws EncryptException {
        try {
            Cipher cipher  = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,key);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch(NoSuchPaddingException | BadPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException e) {
            throw new EncryptException();
        }
    }

    /**
     * Déchiffre une chaîne de caractère à partir d'une clé privé
     * @param encryptedData chaîne de caractère correspondant au chiffrement encodé en base64
     * @param privateKey clé privée utilisée pour le déchiffrement
     * @return une chaîne de caractère du chiffrement en clair
     */
    public String B64decryptData(String encryptedData, PrivateKey privateKey) throws DecryptException {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new DecryptException();
        }
    }
}