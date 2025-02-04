package fr.umontpellier.iut.authserverforge;

import fr.umontpellier.iut.authserverforge.exceptions.NonExistentUsernameException;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

public class TokensManagerTests {

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
    public void isTokenValidShouldReturnFalseWhenTokenInvalidFormat() {
        TokensManager tokensManager = new TokensManager();

        assertFalse(tokensManager.isTokenValid("wrong"));
    }

    @Test
    public void createSessionShouldReturnToken() {
        TokensManager tokensManager = new TokensManager();

        String username = generateRandomString(50);
        String token = tokensManager.createSession(username);

        assertTrue(tokensManager.isTokenValid(token));
    }

    @Test
    public void getTokenShouldReturnToken() {
        TokensManager tokensManager = new TokensManager();

        String username = generateRandomString(50);
        String expectedToken = tokensManager.createSession(username);
        String token = null;

        try {
            token = tokensManager.getToken(username);
        } catch (Exception e) {
            System.err.println(e);
        }

        assertEquals(expectedToken, token);
    }

    @Test
    public void getTokenShouldThrowExceptionWhenInvalidUsername() {
        TokensManager tokensManager = new TokensManager();

        String username = "wrong";

        assertThrows(NonExistentUsernameException.class, () -> {
            tokensManager.getToken(username);
        });
    }

    @Test
    public void getUserFromTokenShouldReturnUsername() {
        TokensManager tokensManager = new TokensManager();

        String expectedUsername = generateRandomString(50);
        String token = tokensManager.createSession(expectedUsername);

        String username = tokensManager.getUserFromToken(token);

        assertEquals(expectedUsername, username);
    }

    @Test
    public void getUserFromTokenShouldReturnNullWhenInvalidToken() {
        TokensManager tokensManager = new TokensManager();

        String token = "wrong";
        String username = tokensManager.getUserFromToken(token);

        assertEquals(null, username);
    }

    @Test
    public void isTokenValidReturnFalseWhenTokenExpired() {
        TokensManager tokensManager = new TokensManager();

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZnNqa2puZmxrc2RqZmlsbXNqZGZtb2premVma2xtanNsZGtmam1sb3planJvbXFqZmxzamZvanpmZGtzem9yZmtzbG1ma3NkZiIsImlhdCI6MTcwNDU4Mjc1MiwiZXhwIjoxNzA0NTgyNzUyfQ.kVSdPFHnpeuevczN5sEomCS30G1gq-ntOrSesN3lhIg";

        assertFalse(tokensManager.isTokenValid(token));
    }
}