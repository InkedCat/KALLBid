package fr.umontpellier.iut.autorite.handler;

import fr.umontpellier.iut.authserverforge.TokensManager;
import fr.umontpellier.iut.autorite.builder.SendWinnerQueryBuilder;
import fr.umontpellier.iut.autorite.utils.BidListValidator;
import fr.umontpellier.iut.autorite.utils.WinnerFinder;
import fr.umontpellier.iut.queryhelper.SignedQuery;
import fr.umontpellier.iut.shared.SignedQueryValidator;
import fr.umontpellier.iut.shared.Winner;
import fr.umontpellier.iut.cryptowrapper.encryption.EncryptionService;
import fr.umontpellier.iut.cryptowrapper.encryption.exceptions.DecryptException;
import fr.umontpellier.iut.cryptowrapper.keys.KeyConverterService;
import fr.umontpellier.iut.cryptowrapper.signature.SignatureService;
import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryHandler;
import fr.umontpellier.iut.queryhelper.QuerySerializer;
import fr.umontpellier.iut.queryhelper.QueryType;
import fr.umontpellier.iut.shared.AuthResult;
import fr.umontpellier.iut.shared.Bid;
import fr.umontpellier.iut.venaut.FindWinnerQuery;
import fr.umontpellier.iut.venaut.SendWinnerQuery;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class FindWinnerQueryHandler implements QueryHandler<FindWinnerQuery> {
    private SignedQueryValidator signedQueryValidator;
    private BidListValidator bidListValidator;
    private SendWinnerQueryBuilder sendWinnerQueryBuilder;
    private WinnerFinder winnerFinder;
    private TokensManager tokensManager;
    private AtomicBoolean isMalicious;

    public FindWinnerQueryHandler(SignedQueryValidator signedQueryValidator, BidListValidator bidListValidator,
                                  SendWinnerQueryBuilder sendWinnerQueryBuilder, WinnerFinder winnerFinder,
                                  TokensManager tokensManager, AtomicBoolean isMalicious) {
        this.signedQueryValidator = signedQueryValidator;
        this.bidListValidator = bidListValidator;
        this.sendWinnerQueryBuilder = sendWinnerQueryBuilder;
        this.winnerFinder = winnerFinder;
        this.tokensManager = tokensManager;
        this.isMalicious = isMalicious;
    }

    @Override
    public Optional<String> handle(FindWinnerQuery query) {

        if(!tokensManager.isTokenValid(query.getToken())) {
            AuthResult authFailQuery = new AuthResult(QueryType.SINGLE, QueryCode.ERROR, "L'authentification à échoué");
            QuerySerializer<AuthResult> querySerializer = new QuerySerializer<>();
            return Optional.of(querySerializer.serialize(authFailQuery));
        }

        if(!signedQueryValidator.validate(query) || query.getBids().isEmpty()
                || !bidListValidator.validateList(query.getBids())) {
            return Optional.empty();
        }

        Optional<Winner> winner;
        try {
            winner = winnerFinder.findWinner(query.getBids());
        } catch (DecryptException e) {
            return Optional.empty();
        }

        if(winner.isEmpty()) {
            return Optional.empty();
        }

        Optional<SendWinnerQuery> sendWinnerQuery = sendWinnerQueryBuilder.build(winner.get());

        if(sendWinnerQuery.isEmpty()) {
            return Optional.empty();
        }

        QuerySerializer<SendWinnerQuery> querySerializer = new QuerySerializer<>();

        return Optional.of(querySerializer.serialize(sendWinnerQuery.get()));
    }
}
