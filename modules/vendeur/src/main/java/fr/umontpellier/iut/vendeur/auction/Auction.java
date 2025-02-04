package fr.umontpellier.iut.vendeur.auction;

import fr.umontpellier.iut.shared.Bid;
import fr.umontpellier.iut.shared.SynchronizedHolder;
import fr.umontpellier.iut.vendeur.bids.BidCouple;
import fr.umontpellier.iut.vendeur.bids.BidList;
import fr.umontpellier.iut.vendeur.bids.BidListService;
import fr.umontpellier.iut.vendeur.bids.strategies.SimpleStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Auction {
    private BidList bidList;
    private BidListService bidListService;
    private SynchronizedHolder<AuctionWinner> winnerHolder;
    private List<BiddingObserver> biddingObservers;
    private boolean finished = false;

    public Auction(BidList bidList, int timeout) {
        this.bidList = bidList;
        this.winnerHolder = new SynchronizedHolder<>(timeout);
        this.bidListService = new BidListService(bidList);
        biddingObservers = new ArrayList<>();
    }

    public Auction(int timeout) {
        this(new BidList(), timeout);
    }

    public void addBid(String signature, String encodedKey, String price) {
        if(finished) return;

        Optional<BidCouple> oldCouple = bidList.removeByKeyAndReturn(encodedKey);

        BidCouple newCouple = new BidCouple(new Bid(signature, encodedKey, price));

        bidList.add(newCouple);

        if(oldCouple.isPresent()) {
            notifyAllObservers(oldCouple.get(), newCouple);
        } else {
            notifyAllObservers(newCouple);
        }
    }

    public void setWinner(AuctionWinner winner) {
        if(finished) return;

        if(bidListService.containsBid(winner.getWinnerBid(), new SimpleStrategy())) {
            winnerHolder.setObject(winner);
            finished = true;
        }
    }

    public Optional<AuctionWinner> getWinner() {
        try {
            return Optional.ofNullable(winnerHolder.getObject());
        } catch (InterruptedException e) {
            finished = true;
            return Optional.empty();
        }
    }

    public Optional<AuctionWinner> getWinnerWait() {
        try {
            return winnerHolder.getObjectWait();
        } catch (InterruptedException e) {
            finished = true;
            return Optional.empty();
        }
    }

    public void addObserver(BiddingObserver observer) {
        biddingObservers.add(observer);
    }

    private void notifyAllObservers(BidCouple newBid) {
        biddingObservers.forEach(observer -> observer.update(newBid));
    }

    private void notifyAllObservers(BidCouple oldBid, BidCouple newBid) {
        biddingObservers.forEach(observer -> observer.update(oldBid, newBid));
    }

    public BidListService getBidListService() {
        return bidListService;
    }

    public boolean hasWinner() {
        return winnerHolder.getAvailablePermits() > 0;
    }

    public boolean hasBids() {
        return !bidList.isEmpty();
    }

    public boolean isFinished() {
        return finished;
    }
}
