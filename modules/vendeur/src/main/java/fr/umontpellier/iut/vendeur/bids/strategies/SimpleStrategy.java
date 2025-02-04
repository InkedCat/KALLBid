package fr.umontpellier.iut.vendeur.bids.strategies;

import fr.umontpellier.iut.vendeur.bids.BidCouple;

import java.util.List;

public class SimpleStrategy implements BidListStrategy{
    @Override
    public List<BidCouple> getBidCouples(List<BidCouple> bids) {
        return bids;
    }
}
