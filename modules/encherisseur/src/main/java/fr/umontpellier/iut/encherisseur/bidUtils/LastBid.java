package fr.umontpellier.iut.encherisseur.bidUtils;

import fr.umontpellier.iut.shared.Bid;

public class LastBid {
    private Bid bid;
    private int decryptedPrice;

    public LastBid(Bid bid, int decryptedPrice) {
        this.bid = bid;
        this.decryptedPrice = decryptedPrice;
    }

    public Bid getBid() {
        return bid;
    }

    public int getDecryptedPrice() {
        return decryptedPrice;
    }

    public boolean hasBid() {
        return bid != null;
    }
}
