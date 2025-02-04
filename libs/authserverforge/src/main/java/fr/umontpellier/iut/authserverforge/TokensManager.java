package fr.umontpellier.iut.authserverforge;

import fr.umontpellier.iut.authserverforge.exceptions.InvalidTokenException;
import fr.umontpellier.iut.authserverforge.exceptions.NonExistentUsernameException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.json.JSONObject;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokensManager {

    private static final String USER_TOKEN_FILE = "user-token.json";
    private static final long TOKEN_EXPIRATION_TIME = 3600000;
    private final Map<String, String> tokensMap;
    private static final String JWT_SECRET = "e051ViRfTGgqZ2Q0NjNqRjM2QC44OHkhREdea3U3WFY";

    public TokensManager() {
        this.tokensMap = loadTokens();
    }

    private Map<String, String> loadTokens() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_TOKEN_FILE))) {
            return (Map<String, String>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des tokens : " + e.getMessage());

            return new HashMap<>();
        }
    }

    private void saveTokens() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_TOKEN_FILE))) {
            oos.writeObject(tokensMap);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des tokens : " + e.getMessage());
        }
    }

    /**
     * Renvois un token valide, lève une erreur si le nom d'utilisateur n'a pas de token associé ou si le token est invalide.
     * @param username nom d'utilisateur
     * @return un token valide pour l'utilisateur fourni sous la forme d'un String
     * @throws NonExistentUsernameException
     * @throws InvalidTokenException
     */
    public String getToken(String username) throws NonExistentUsernameException, InvalidTokenException {
        if (tokensMap.containsKey(username)) {
            String existingToken = tokensMap.get(username);
            if (isTokenValid(existingToken)) {
                return existingToken;
            } else {
                throw new InvalidTokenException();
            }
        } else {
            throw new NonExistentUsernameException();
        }
    }

    /**
     * Vérifie si un token existe dans la base de données et s'il n'a pas expiré
     * @param token sous la forme d'un String
     * @return true ou false en fonction de la validité du token fourni
     */
    public boolean isTokenValid(String token) {
        try {
            if (tokensMap.containsValue(token)) {
                Jwts.parser().verifyWith(convertStringToSecretKeyto(JWT_SECRET)).build().parse(token);
                return true;
            }
            return false;
        } catch (ExpiredJwtException expiredJwtException) {
            tokensMap.remove(token);
            return false;
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    private static SecretKey convertStringToSecretKeyto(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }

    /**
     * Extrait le nom d'utilisateur d'un token valid
     * @param token sous la forme d'un String
     * @return le nom d'utilisateur associé au token donné
     */
    public String getUserFromToken(String token) {
        if (isTokenValid(token)) {
            String[] tokenParts = token.split("\\.");

            String encodedData = tokenParts[1];
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(encodedData);
            String decodedData = new String(decodedBytes);

            JSONObject json = new JSONObject(decodedData);

            return json.getString("sub");
        }
        return null;
    }

    /**
     * Créé une nouvelle session pour un utilisateur
     * @param username nom d'utilisateur pour lequel la session doit être créée
     * @return le token de la session qui viens d'être créée
     */
    public String createSession(String username) {
        String token = TokensManager.generateToken(username);
        tokensMap.put(username, token);
        saveTokens();

        return token;
    }

    private static String generateToken(String username) {
        var claimsBuilder = Jwts.claims().subject(username);

        var claims = claimsBuilder.build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(convertStringToSecretKeyto(JWT_SECRET), Jwts.SIG.HS256)
                .compact();
    }
}