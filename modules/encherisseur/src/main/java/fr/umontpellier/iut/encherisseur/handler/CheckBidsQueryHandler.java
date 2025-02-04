package fr.umontpellier.iut.encherisseur.handler;

import fr.umontpellier.iut.autench.MaliciousAuctionQuery;
import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;
import fr.umontpellier.iut.encherisseur.auction.Auction;
import fr.umontpellier.iut.encherisseur.exceptions.MaliciousAuctionException;
import fr.umontpellier.iut.enchven.BiddingQuery;
import fr.umontpellier.iut.enchven.CheckBidsQuery;
import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryHandler;
import fr.umontpellier.iut.queryhelper.QuerySerializer;
import fr.umontpellier.iut.queryhelper.QueryType;
import fr.umontpellier.iut.shared.Bid;
import fr.umontpellier.iut.shared.BidValidator;
import fr.umontpellier.iut.shared.SignedQueryValidator;
import fr.umontpellier.iut.socketwrapper.ConnectionRefresher;
import fr.umontpellier.iut.socketwrapper.exceptions.RefresherClosedException;
import fr.umontpellier.iut.socketwrapper.exceptions.RemoteUnavailableException;
import fr.umontpellier.iut.socketwrapper.exceptions.SocketClosedException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Optional;

public class CheckBidsQueryHandler implements QueryHandler<CheckBidsQuery> {

    private Auction auction;
    private SignedQueryValidator signedQueryValidator;
    private BidValidator bidValidator;
    private ConnectionRefresher autorite;

    public CheckBidsQueryHandler(Auction auction, SignedQueryValidator signedQueryValidator, BidValidator bidValidator,
                                 ConnectionRefresher autorite) {
        this.auction = auction;
        this.signedQueryValidator = signedQueryValidator;
        this.bidValidator = bidValidator;
        this.autorite = autorite;
    }

    private boolean containBid(CheckBidsQuery query) throws MaliciousAuctionException {
        for(Bid bid : query.getBids()) {
            if(!bidValidator.validateBid(bid)) {
                throw new MaliciousAuctionException();
            }

            if(auction.getLastBid().getBid().price().equals(bid.price())) {
                return true;
            }
        }

        return false;
    }

    private String resendBid() {
        BiddingQuery bidQuery = new BiddingQuery(QueryType.SINGLE, QueryCode.SUCCESS, auction.getLastBid().getBid());
        QuerySerializer<BiddingQuery> serializer = new QuerySerializer<>();

        return serializer.serialize(bidQuery);
    }

    @Override
    public Optional<String> handle(CheckBidsQuery query) {
        MaliciousAuctionQuery maliciousAuctionQuery = new MaliciousAuctionQuery();
        QuerySerializer<MaliciousAuctionQuery> serializer = new QuerySerializer<>();

        if(!signedQueryValidator.validate(query)) {
            try {
                autorite.getConnection().send(serializer.serialize(maliciousAuctionQuery));
            } catch (SocketClosedException | RemoteUnavailableException | RefresherClosedException | IOException ignore) {}

            return Optional.empty();
        }

        boolean present;
        try {
            present = containBid(query);
        } catch (MaliciousAuctionException ignored) {
            try {
                autorite.getConnection().send(serializer.serialize(maliciousAuctionQuery));
            } catch (SocketClosedException | RemoteUnavailableException | RefresherClosedException | IOException ignore) {}

            return Optional.empty();
        }

        if(!present) {
            return Optional.of(resendBid());
        }

        return Optional.empty();
    }
}
