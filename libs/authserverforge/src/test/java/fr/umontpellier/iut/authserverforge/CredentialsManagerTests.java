package fr.umontpellier.iut.authserverforge;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

public class CredentialsManagerTests {

    public static String generateRandomString(int longueur) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder randomString= new StringBuilder(longueur);

        for (int i = 0; i < longueur; i++) {
            int index = random.nextInt(caracteres.length());
            randomString.append(caracteres.charAt(index));
        }

        return randomString.toString();
    }

    @Test
    public void userExistsShouldReturnFalseWhenUserDoesntExist() {

        CredentialsManager credentialsManager = new CredentialsManager();

        String username = generateRandomString(50);

        assertFalse(credentialsManager.userExists(username));
    }

    @Test
    public void createUserAccountShouldAddUser() {

        CredentialsManager credentialsManager = new CredentialsManager();

        String username = generateRandomString(50);
        String password = generateRandomString(50);

        credentialsManager.createUserAccount(username, password);
        assertTrue(credentialsManager.userExists(username));
    }

    @Test
    public void authenticateUserShouldReturnFalseWhenUserDoesntExist() {

        CredentialsManager credentialsManager = new CredentialsManager();

        assertFalse(credentialsManager.authenticateUser("wrong", "wrong"));
    }

    @Test
    public void authenticateUserShouldReturnFalseWhenPasswordIncorrect() {

        CredentialsManager credentialsManager = new CredentialsManager();

        String username = generateRandomString(50);
        String password = generateRandomString(50);

        credentialsManager.createUserAccount(username, password);

        assertFalse(credentialsManager.authenticateUser(username, "wrong"));
    }

    @Test
    public void authenticateUserShouldReturnTrueWhenPasswordCorrect() {

        CredentialsManager credentialsManager = new CredentialsManager();

        String username = generateRandomString(50);
        String password = generateRandomString(50);

        credentialsManager.createUserAccount(username, password);

        assertTrue(credentialsManager.authenticateUser(username, password));
    }
}