package fr.umontpellier.iut.vendeur.bids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BidList {

    private final List<BidCouple> bids;

    public BidList(List<BidCouple> bids) {
        this.bids = Collections.synchronizedList(bids);
    }

    public BidList() {
        this(new ArrayList<>());
    }

    public void add(BidCouple bidCouple) {
        bids.add(bidCouple);
    }

    public Optional<BidCouple> removeByKeyAndReturn(String encodedKey) {
        Optional<BidCouple> bidCouple = bids.stream()
                .filter(bid -> bid.getBid().encodedKey().equals(encodedKey))
                .findFirst();

        bidCouple.ifPresent(bids::remove);

        return bidCouple;
    }

    public List<BidCouple> getBidCouples() {
        return bids;
    }

    public boolean isEmpty() {
        return bids.isEmpty();
    }

    public void clearList() {
        bids.clear();
    }
}
