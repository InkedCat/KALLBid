package fr.umontpellier.iut.authserverforge.exceptions;

public class NonExistentUsernameException extends Exception {
    public NonExistentUsernameException() {
        super("le nom d'utilisateur n'existe pas dans la base de données");
    }
}