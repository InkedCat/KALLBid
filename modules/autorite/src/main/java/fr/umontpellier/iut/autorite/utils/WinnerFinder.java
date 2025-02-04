package fr.umontpellier.iut.autorite.utils;

import fr.umontpellier.iut.cryptowrapper.encryption.exceptions.DecryptException;
import fr.umontpellier.iut.shared.Bid;
import fr.umontpellier.iut.shared.Winner;

import java.util.List;
import java.util.Optional;

public class WinnerFinder {

    private BidDecryptor bidDecryptor;

    public WinnerFinder(BidDecryptor bidDecryptor) {
        this.bidDecryptor = bidDecryptor;
    }

    public Optional<Winner> findWinner(List<Bid> bids) throws DecryptException {
        int maxPriceIndex = -1;
        int maxPrice = Integer.MIN_VALUE;
        int secondMaxPrice = Integer.MIN_VALUE;

        for (int i = 0; i < bids.size(); i++) {
            int price = bidDecryptor.decrypt(bids.get(i).price());

            if (price > maxPrice) {
                secondMaxPrice = maxPrice;
                maxPrice = price;
                maxPriceIndex = i;
            } else if (price < maxPrice && price > secondMaxPrice) {
                secondMaxPrice = price;
            }
        }

        if(maxPriceIndex == -1 || secondMaxPrice == Integer.MIN_VALUE) {
            return Optional.empty();
        }

        return Optional.of(new Winner(bids.get(maxPriceIndex).price(), secondMaxPrice));
    }
}
