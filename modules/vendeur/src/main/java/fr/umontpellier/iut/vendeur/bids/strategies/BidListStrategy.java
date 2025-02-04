package fr.umontpellier.iut.vendeur.bids.strategies;

import fr.umontpellier.iut.vendeur.bids.BidCouple;

import java.util.List;

public interface BidListStrategy {

    List<BidCouple> getBidCouples(List<BidCouple> bids);
}
