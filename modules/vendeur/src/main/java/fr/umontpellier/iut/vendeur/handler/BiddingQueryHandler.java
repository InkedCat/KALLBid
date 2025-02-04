package fr.umontpellier.iut.vendeur.handler;

import fr.umontpellier.iut.enchven.BiddingQuery;
import fr.umontpellier.iut.queryhelper.QueryHandler;
import fr.umontpellier.iut.shared.SignedQueryValidator;
import fr.umontpellier.iut.vendeur.auction.Auction;

import java.util.Optional;

public class BiddingQueryHandler implements QueryHandler<BiddingQuery> {
    private final Auction auction;
    private final SignedQueryValidator signedQueryValidator;

    public BiddingQueryHandler(Auction auction, SignedQueryValidator signedQueryValidator) {
        this.auction = auction;
        this.signedQueryValidator = signedQueryValidator;
    }

    @Override
    public Optional<String> handle(BiddingQuery query) {
        if(!signedQueryValidator.validate(query)) return Optional.empty();

        auction.addBid(query.getSignature(), query.getEncodedKey(), query.getPrice());

        return Optional.empty();
    }
}
