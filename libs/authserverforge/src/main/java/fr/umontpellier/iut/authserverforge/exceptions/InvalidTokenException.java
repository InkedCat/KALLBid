package fr.umontpellier.iut.authserverforge.exceptions;

public class InvalidTokenException extends Exception {
    public InvalidTokenException() {
        super("token invalide");
    }
}