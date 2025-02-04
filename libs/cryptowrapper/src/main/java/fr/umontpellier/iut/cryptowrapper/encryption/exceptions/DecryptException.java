package fr.umontpellier.iut.cryptowrapper.encryption.exceptions;

public class DecryptException extends Exception {
    public DecryptException() {
        super("Impossible de déchiffrer le message");
    }
}