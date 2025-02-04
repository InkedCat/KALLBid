package fr.umontpellier.iut.socketwrapper.exceptions;

public class SocketClosedException extends Exception {

    public SocketClosedException(String message, Exception e) {
        super(message, e);
    }

    public SocketClosedException(String message) {
        super(message);
    }
}
