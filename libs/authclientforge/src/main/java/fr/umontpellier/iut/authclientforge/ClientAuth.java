package fr.umontpellier.iut.authclientforge;

import fr.umontpellier.iut.shared.AuthQuery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ClientAuth {

    public static boolean checkPasswordRules(String password) {
        Pattern specialCharacterPattern = Pattern.compile("[^a-zA-Z0-9]");
        Pattern injectionSpecialCharacterPattern = Pattern.compile("[,':;\" ]");
        Matcher matcherSpecialCharacter = specialCharacterPattern.matcher(password);
        Matcher matcherInjectionCharacter = injectionSpecialCharacterPattern.matcher(password);

        if (password.length() < 8 || !matcherSpecialCharacter.find() || matcherInjectionCharacter.find()) {
            System.out.println("<erreur> commande incorrect : le mot de passe doit faire au moins 8 caractères et doit contenir au moins un caractère spécial (, ' : ; \" exclues)");
            return false;
        }

        return true;
    }

    public abstract AuthQuery createAuthQuery(String username, String password);
}