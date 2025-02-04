package fr.umontpellier.iut.vendeur.auction;

import fr.umontpellier.iut.vendeur.bids.BidCouple;

public interface BiddingObserver {
    void update(BidCouple oldBid, BidCouple newBid);

    void update(BidCouple newBid);
}
