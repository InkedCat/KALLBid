package fr.umontpellier.iut.vendeur.handler;

import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;
import fr.umontpellier.iut.queryhelper.QueryHandler;
import fr.umontpellier.iut.shared.SignedQueryValidator;
import fr.umontpellier.iut.venaut.SendWinnerQuery;
import fr.umontpellier.iut.vendeur.auction.AuctionWinner;
import fr.umontpellier.iut.vendeur.bids.BidCouple;
import fr.umontpellier.iut.vendeur.auction.Auction;
import fr.umontpellier.iut.vendeur.bids.strategies.BidListStrategy;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Optional;

public class SendWinnerQueryHandler implements QueryHandler<SendWinnerQuery> {
    private Auction auction;
    private SignedQueryValidator signedQueryValidator;
    private BidListStrategy bidListStrategy;

    public SendWinnerQueryHandler(Auction auction, SignedQueryValidator signedQueryValidator,
                                  BidListStrategy bidListStrategy) {
        this.auction = auction;
        this.signedQueryValidator = signedQueryValidator;
        this.bidListStrategy = bidListStrategy;
    }

    @Override
    public Optional<String> handle(SendWinnerQuery query) {

        if(!signedQueryValidator.validate(query)) return Optional.empty();


        BidCouple winnerBid = auction.getBidListService()
                .getBidCoupleFromPrice(query.getEncryptedPrice(), bidListStrategy);

        if(winnerBid == null) return Optional.empty();

        AuctionWinner winner = new AuctionWinner(winnerBid.getBid(), query.getSecondPrice());

        auction.setWinner(winner);

        return Optional.empty();
    }
}
