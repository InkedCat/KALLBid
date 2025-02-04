package fr.umontpellier.iut.authserverforge;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;

public class CredentialsManager {

    private static final String USER_DATA_FILE = "user-data.json";
    private final Map<String, String> credentials;
    private static final int LOG_ROUNDS = 12;

    public CredentialsManager() {
        this.credentials = loadCredentials();
    }

    private Map<String, String> loadCredentials() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
            return (Map<String, String>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des informations d'authentification : " + e.getMessage());

            return new HashMap<>();
        }
    }

    private void saveCredentials() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(credentials);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des informations d'authentification : " + e.getMessage());
        }
    }

    /**
     * Créé un nouvel utilisateur et l'enregistre dans la base de données.
     * @param username nom d'utilisateur
     * @param password mot de passe de l'utilisateur
     */
    public void createUserAccount(String username, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));
        credentials.put(username, hashedPassword);
        saveCredentials();
        System.out.println("Log (serveur): l'utilisateur<enchérisseur> à été créé");
    }

    /**
     * Vérifie si un utilisateur existe dans la base de données et si le mot de passe fournie correspond à celui enregistré dans la base de données.
     * @param username nom d'utilisateur
     * @param password mot de passe de l'utilisateur
     * @return true ou false en fonction de si les identifiants fournis correspondent
     */
    public boolean authenticateUser(String username, String password) {
        String hashedPassword = credentials.get(username);

        return hashedPassword != null && BCrypt.checkpw(password, hashedPassword);
    }

    /**
     * Vérifie si un utilisateur existe dans la base de données.
     * @param username nom d'utilisateur
     * @return true ou false en fonction de la présence du nom d'utilisateur dans la base de données
     */
    public boolean userExists(String username) {
        return credentials.containsKey(username);
    }
}