package fr.umontpellier.iut.autorite.handler;

import fr.umontpellier.iut.autench.MaliciousAuctionQuery;
import fr.umontpellier.iut.queryhelper.QueryHandler;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class MaliciousAuctionQueryHandler implements QueryHandler<MaliciousAuctionQuery> {

    AtomicBoolean isMalicious;

    public MaliciousAuctionQueryHandler(AtomicBoolean isMalicious) {
        this.isMalicious = isMalicious;
    }

    @Override
    public Optional<String> handle(MaliciousAuctionQuery query) {
        if(!isMalicious.get()) isMalicious.set(true);

        return Optional.empty();
    }
}
