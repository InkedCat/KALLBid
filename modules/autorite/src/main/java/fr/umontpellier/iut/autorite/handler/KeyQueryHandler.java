package fr.umontpellier.iut.autorite.handler;

import fr.umontpellier.iut.autench.KeyQuery;
import fr.umontpellier.iut.autench.SendKeyQuery;
import fr.umontpellier.iut.authserverforge.TokensManager;
import fr.umontpellier.iut.autorite.builder.SendKeyQueryBuilder;
import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryHandler;
import fr.umontpellier.iut.queryhelper.QuerySerializer;
import fr.umontpellier.iut.queryhelper.QueryType;
import fr.umontpellier.iut.shared.AuthResult;

import java.util.Optional;

public class KeyQueryHandler implements QueryHandler<KeyQuery> {

    private TokensManager tokensManager;
    private SendKeyQueryBuilder sendKeyQueryBuilder;

    public KeyQueryHandler(TokensManager tokensManager, SendKeyQueryBuilder sendKeyQueryBuilder) {
        this.tokensManager = tokensManager;
        this.sendKeyQueryBuilder = sendKeyQueryBuilder;
    }

    private AuthResult buildAuthFailQuery() {
        return new AuthResult(QueryType.SINGLE, QueryCode.ERROR, "L'authentification à échoué");
    }

    @Override
    public Optional<String> handle(KeyQuery query) {
        QuerySerializer<AuthResult> querySerializer = new QuerySerializer<>();

        if(!tokensManager.isTokenValid(query.getToken())) {
            return Optional.of(querySerializer.serialize(buildAuthFailQuery()));
        }

        QuerySerializer<SendKeyQuery> serializer = new QuerySerializer<>();
        Optional<SendKeyQuery> keyQuery = sendKeyQueryBuilder.build();

        return keyQuery.map(serializer::serialize).or(() -> Optional.of(querySerializer.serialize(buildAuthFailQuery())));
    }
}
