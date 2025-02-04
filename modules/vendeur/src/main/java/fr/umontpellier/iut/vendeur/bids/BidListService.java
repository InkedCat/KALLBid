package fr.umontpellier.iut.vendeur.bids;

import fr.umontpellier.iut.shared.Bid;
import fr.umontpellier.iut.vendeur.bids.strategies.BidListStrategy;

import java.util.List;

public class BidListService {

    private BidList bidList;

    public BidListService(BidList bidList) {
        this.bidList = bidList;
    }

    public List<BidCouple> getBidCouples(BidListStrategy strategy) {
        return strategy.getBidCouples(bidList.getBidCouples());
    }

    public List<Bid> getToBidList(BidListStrategy strategy) {
        return getBidCouples(strategy).stream().map(BidCouple::getBid).toList();
    }

    public boolean containsBid(Bid bid, BidListStrategy strategy) {
        return getBidCouples(strategy).stream().anyMatch(bidCouple -> bidCouple.getBid().equals(bid));
    }

    public BidCouple getBidCoupleFromPrice(String price, BidListStrategy strategy) {
        for(BidCouple bidCouple : getBidCouples(strategy)) {
            if(bidCouple.getBid().price().equals(price)) {
                return bidCouple;
            }
        }

        return null;
    }
}
