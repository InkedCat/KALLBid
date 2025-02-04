package fr.umontpellier.iut.socketwrapper.exceptions;

public class RefresherClosedException extends Exception {

    public RefresherClosedException(String message, Exception cause) {
        super(message, cause);
    }

    public RefresherClosedException(String message) {
        super(message);
    }
}
