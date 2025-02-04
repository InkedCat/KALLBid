package fr.umontpellier.iut.autorite.handler;

import fr.umontpellier.iut.authserverforge.CredentialsManager;
import fr.umontpellier.iut.authserverforge.TokensManager;
import fr.umontpellier.iut.authserverforge.exceptions.InvalidTokenException;
import fr.umontpellier.iut.authserverforge.exceptions.NonExistentUsernameException;
import fr.umontpellier.iut.queryhelper.*;
import fr.umontpellier.iut.shared.AuthQuery;
import fr.umontpellier.iut.shared.AuthResult;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthQueryHandler implements QueryHandler<AuthQuery> {

    private CredentialsManager credentialsManager;
    private TokensManager tokensManager;

    public AuthQueryHandler(CredentialsManager credentialsManager, TokensManager tokensManager) {
        this.credentialsManager = credentialsManager;
        this.tokensManager = tokensManager;
    }

    public Optional<String> handle(AuthQuery query) {
        System.out.println("Log (serveur): données reçues du client<enchérisseur> = " + query);

        try {
            String token = optionHandler(query);
            AuthResult response = new AuthResult(QueryType.SINGLE, QueryCode.SUCCESS, token);
            QuerySerializer<AuthResult> querySerializer = new QuerySerializer<>();
            return Optional.of(querySerializer.serialize(response));
        } catch (IOException e) {
            AuthResult response = new AuthResult(QueryType.SINGLE, QueryCode.ERROR, e.getMessage());
            QuerySerializer<AuthResult> querySerializer = new QuerySerializer<>();
            return Optional.of(querySerializer.serialize(response));
        }
    }

    private String optionHandler(AuthQuery query) throws IOException {
        if (checkPasswordRules(query)) {
            int option = query.getOption();
            if (option == 1) {
                return createAccount(query);
            } else if (option == 2) {
                return authenticateUser(query);
            } else {
                throw new IOException("Option d'authentification invalide");
            }
        } else {
            throw new IOException("Le mot de passe doit faire au moins 8 caractères et doit contenir un caractère spécial");
        }
    }

    private boolean checkPasswordRules(AuthQuery query) {
        Pattern specialCharacterPattern = Pattern.compile("[^a-zA-Z0-9]");
        Pattern injectionSpecialCharacterPattern = Pattern.compile("[,':;\" ]");

        String password = query.getPassword();

        Matcher matcherSpecialCharacter = specialCharacterPattern.matcher(password);
        Matcher matcherInjectionCharacter = injectionSpecialCharacterPattern.matcher(password);

        if (password.length() < 8 || !matcherSpecialCharacter.find() || matcherInjectionCharacter.find()) {
            System.out.println("Log (serveur): le mot de passe reçu ne respecte pas les règles de sécurité nécessaire");
            return false;
        }

        return true;
    }

    private String createAccount(AuthQuery query) throws IOException {
        String username = query.getUsername();
        String password = query.getPassword();
        if (credentialsManager.userExists(username)) {
            System.out.println("Log (serveur): un nom d'utilisateur<enchérisseur> similaire existe déjà");
            throw new IOException("Un nom d'utilisateur similaire existe déjà, veuillez en sélectionner un autre");
        } else {
            credentialsManager.createUserAccount(username, password);
            String token;
            try {
                token = tokensManager.getToken(username);
            } catch (InvalidTokenException | NonExistentUsernameException exception) {
                token = tokensManager.createSession(username);
            }
            return token;
        }
    }

    private String authenticateUser(AuthQuery query) throws IOException {
        String username = query.getUsername();
        String password = query.getPassword();
        if (credentialsManager.userExists(username)) {
            if (credentialsManager.authenticateUser(username, password)) {
                System.out.println("Log (serveur): l'authentification du client<enchérisseur> à réussi");
                String token;
                try {
                    token = tokensManager.getToken(username);
                } catch (InvalidTokenException | NonExistentUsernameException exception) {
                    token = tokensManager.createSession(username);
                }
                return token;
            } else {
                System.out.println("Log (serveur): échec de l'authentification du client<enchérisseur>, mot de passe incorrect");
                throw new IOException("Le mot de passe est incorrect");
            }
        } else {
            System.out.println("Log (serveur): utilisateur<enchérisseur> introuvable");
            throw new IOException("Utilisateur introuvable, veuillez selectionner un nom d'utilisateur valide ou créer un compte");
        }
    }
}