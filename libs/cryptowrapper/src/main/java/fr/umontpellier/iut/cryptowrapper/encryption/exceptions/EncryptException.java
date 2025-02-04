package fr.umontpellier.iut.cryptowrapper.encryption.exceptions;

public class EncryptException extends Exception {
    public EncryptException() {
        super("Impossible de chiffrer le message");
    }
}