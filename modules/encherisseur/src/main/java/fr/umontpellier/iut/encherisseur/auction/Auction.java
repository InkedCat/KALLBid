package fr.umontpellier.iut.encherisseur.auction;

import fr.umontpellier.iut.encherisseur.bidUtils.KeyHolder;
import fr.umontpellier.iut.encherisseur.bidUtils.LastBid;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Auction {
    private LastBid lastBid;
    private int secondPrice;
    private KeyHolder<PublicKey> autoriteKey;
    private List<SecondPriceObserver> secondPriceObservers;

    public Auction(LastBid lastBid, KeyHolder<PublicKey> autoriteKey) {
        this.lastBid = lastBid;
        this.autoriteKey = autoriteKey;
        secondPriceObservers = Collections.synchronizedList(new ArrayList<>());
    }

    public Auction(KeyHolder<PublicKey> autoriteKey) {
        this(null, autoriteKey);
    }

    public LastBid getLastBid() {
        return lastBid;
    }

    public void setLastBid(LastBid bid) {
        lastBid = bid;
    }

    public boolean hasBid() {
        return lastBid != null;
    }

    public void setAutoriteKey(PublicKey key) {
        autoriteKey.setKey(key);
    }

    public Optional<PublicKey> getAutoriteKey() {
        try {
            return Optional.ofNullable(autoriteKey.getKey());
        } catch (InterruptedException e) {
            return Optional.empty();
        }
    }

    public Optional<PublicKey> getAutoriteKeyWait() {
        try {
            return autoriteKey.getKeyWait();
        } catch (InterruptedException e) {
            return Optional.empty();
        }
    }

    public void setSecondPrice(int secondPrice) {
        this.secondPrice = secondPrice;
        notifyObservers();
    }

    public void subscribeSecondPrice(SecondPriceObserver observer) {
        secondPriceObservers.add(observer);
    }

    private void notifyObservers() {
        secondPriceObservers.forEach(observer -> observer.update(secondPrice));
    }

    public boolean hasAutoriteExpired() {
        return autoriteKey.hasExpired();
    }

}
