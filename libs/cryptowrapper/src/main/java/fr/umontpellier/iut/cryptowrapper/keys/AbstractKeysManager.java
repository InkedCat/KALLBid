package fr.umontpellier.iut.cryptowrapper.keys;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

public abstract class AbstractKeysManager {

    private String ALGORITHM;
    private String PUBLIC_KEY_FILE;
    private String PRIVATE_KEY_FILE;
    private int KEYS_SIZE;

    public AbstractKeysManager(String publicFileName, String privateFileName, String algorithm, int keysSize) {
        if (!Objects.equals(publicFileName, privateFileName)) {
            PUBLIC_KEY_FILE = publicFileName;
            PRIVATE_KEY_FILE = privateFileName;
            ALGORITHM = algorithm;
            KEYS_SIZE = keysSize;
        } else {
            throw new RuntimeException("les noms pour les fichiers de stockages doivent être différents entre eux");
        }

    }

    /**
     * Génère une paire de clés
     * @return la paire de clés
     */
    public KeyPair generateKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(KEYS_SIZE);

            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | NullPointerException e) {
            throw new RuntimeException("Impossible de générer la paire de clés");
        }
    }

    /**
     * Enregistre les clés privée et publique dans leurs fichiers de stockage respectifs
     * @param keyPair paire de clés à écrire
     */
    public void writeKeyPair(KeyPair keyPair) {
        try {
            try (FileOutputStream fos = new FileOutputStream(PUBLIC_KEY_FILE)) {
                fos.write(keyPair.getPublic().getEncoded());
            }

            try (FileOutputStream fos = new FileOutputStream(PRIVATE_KEY_FILE)) {
                fos.write(keyPair.getPrivate().getEncoded());
            }
        }  catch (NullPointerException | SecurityException | IOException e) {
            throw new RuntimeException("Impossible d'écrire la paire de clés de façon persistante");
        }
    }

    /**
     * Charge depuis le fichier de stockage la clé publique
     * @return la clé publique
     */
    public PublicKey loadPublicKey() {
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(PUBLIC_KEY_FILE));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return keyFactory.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Impossible de charger la clé publique");
        }
    }

    /**
     * Charge depuis le fichier de stockage la clé privée
     * @return la clé privée
     */
    public PrivateKey loadPrivateKey() {
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE));
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return keyFactory.generatePrivate(spec);
        } catch (IOException |NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Impossible de charger la clé privée");
        }
    }

    /**
     * Vérifie l'existence d'une paire de clés
     * @return true ou false en fonction de l'existence ou non d'une paire de clés
     */
    public boolean keyPairExists() {
        Path publicKeyPath = Paths.get(PUBLIC_KEY_FILE);
        Path privateKeyPath = Paths.get(PRIVATE_KEY_FILE);
        return Files.exists(publicKeyPath) && Files.exists(privateKeyPath);
    }

}