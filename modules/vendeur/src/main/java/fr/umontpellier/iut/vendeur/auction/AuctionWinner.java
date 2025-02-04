package fr.umontpellier.iut.vendeur.auction;

import fr.umontpellier.iut.shared.Bid;

public class AuctionWinner {
    private Bid winnerBid;
    private int secondPrice;

    public AuctionWinner(Bid winnerBid, int secondPrice) {
        this.winnerBid = winnerBid;
        this.secondPrice = secondPrice;
    }

    public Bid getWinnerBid() {
        return winnerBid;
    }

    public int getSecondPrice() {
        return secondPrice;
    }
}
