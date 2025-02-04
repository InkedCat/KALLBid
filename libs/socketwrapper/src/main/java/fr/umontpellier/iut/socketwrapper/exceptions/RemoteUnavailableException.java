package fr.umontpellier.iut.socketwrapper.exceptions;

public class RemoteUnavailableException extends Exception {

    public RemoteUnavailableException(String message, Exception e) {
        super(message, e);
    }

    public RemoteUnavailableException(String message) {
        super(message);
    }
}
