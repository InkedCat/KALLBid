package fr.umontpellier.iut.autorite.utils;

import fr.umontpellier.iut.shared.Bid;
import fr.umontpellier.iut.shared.BidValidator;

import java.util.List;

public class BidListValidator {

    private BidValidator bidValidator;

    public BidListValidator(BidValidator bidValidator) {
        this.bidValidator = bidValidator;
    }

    public boolean validateList(List<Bid> bids) {
        for (Bid bid : bids) {
            if (!bidValidator.validateBid(bid)) {
                return false;
            }
        }

        return true;
    }
}
