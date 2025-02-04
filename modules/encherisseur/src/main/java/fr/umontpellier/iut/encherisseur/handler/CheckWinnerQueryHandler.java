package fr.umontpellier.iut.encherisseur.handler;

import fr.umontpellier.iut.autench.MaliciousAuctionQuery;
import fr.umontpellier.iut.encherisseur.auction.Auction;
import fr.umontpellier.iut.enchven.CheckWinnerQuery;
import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryHandler;
import fr.umontpellier.iut.queryhelper.QuerySerializer;
import fr.umontpellier.iut.queryhelper.QueryType;
import fr.umontpellier.iut.shared.SignedQueryValidator;
import fr.umontpellier.iut.socketwrapper.ConnectionRefresher;
import fr.umontpellier.iut.socketwrapper.exceptions.RefresherClosedException;
import fr.umontpellier.iut.socketwrapper.exceptions.RemoteUnavailableException;
import fr.umontpellier.iut.socketwrapper.exceptions.SocketClosedException;

import java.io.IOException;
import java.util.Optional;

public class CheckWinnerQueryHandler implements QueryHandler<CheckWinnerQuery> {

    private Auction auction;
    private ConnectionRefresher autorite;
    private SignedQueryValidator signedQueryValidator;

    public CheckWinnerQueryHandler(Auction auction, ConnectionRefresher autorite,
                                   SignedQueryValidator signedQueryValidator){
        this.auction = auction;
        this.autorite = autorite;
        this.signedQueryValidator = signedQueryValidator;
    }

    @Override
    public Optional<String> handle(CheckWinnerQuery query) {
        MaliciousAuctionQuery maliciousAuctionQuery = new MaliciousAuctionQuery();
        QuerySerializer<MaliciousAuctionQuery> serializer = new QuerySerializer<>();

        if(!signedQueryValidator.validate(query)) {
            try {
                autorite.getConnection().send(serializer.serialize(maliciousAuctionQuery));
            } catch (SocketClosedException | RemoteUnavailableException | RefresherClosedException | IOException ignore) {}

            return Optional.empty();
        }

        auction.setSecondPrice(query.getPrice());

        return Optional.empty();
    }
}
