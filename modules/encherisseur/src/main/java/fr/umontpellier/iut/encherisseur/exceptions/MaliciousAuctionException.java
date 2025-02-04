package fr.umontpellier.iut.encherisseur.exceptions;

public class MaliciousAuctionException extends Exception {
    public MaliciousAuctionException() {
        super("Enchère malicieuse détectée");
    }
}
