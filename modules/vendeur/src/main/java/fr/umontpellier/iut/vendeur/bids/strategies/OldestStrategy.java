package fr.umontpellier.iut.vendeur.bids.strategies;

import fr.umontpellier.iut.vendeur.bids.BidCouple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OldestStrategy implements BidListStrategy {

    private BidListStrategy strategy;

    public OldestStrategy(BidListStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public List<BidCouple> getBidCouples(List<BidCouple> bids) {
        List<BidCouple> base = strategy.getBidCouples(bids);

        Map<String, BidCouple> bidMap = new HashMap<>();
        for (BidCouple bidCouple : base) {
            String price = bidCouple.getBid().price();
            if (bidMap.containsKey(price)) {
                BidCouple existingBidCouple = bidMap.get(price);
                if (bidCouple.isOlderThan(existingBidCouple)) {
                    bidMap.put(price, bidCouple);
                }
            } else {
                bidMap.put(price, bidCouple);
            }
        }

        return new ArrayList<>(bidMap.values());
    }
}
