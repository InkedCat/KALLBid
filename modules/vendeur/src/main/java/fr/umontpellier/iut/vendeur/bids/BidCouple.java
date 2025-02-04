package fr.umontpellier.iut.vendeur.bids;

import fr.umontpellier.iut.shared.Bid;

import java.time.LocalDateTime;
import java.util.Objects;

public class BidCouple {

    private Bid bid;

    private LocalDateTime date;

    public BidCouple(Bid bid, LocalDateTime date) {
        this.bid = bid;
        this.date = date;
    }

    public BidCouple(Bid bid) {
        this(bid, LocalDateTime.now());
    }

    public Bid getBid() {
        return bid;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public boolean isOlderThan(BidCouple other) {
        return date.isBefore(other.date);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        BidCouple bidCouple = (BidCouple) object;
        return Objects.equals(bid, bidCouple.bid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bid);
    }
}
