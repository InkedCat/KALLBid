package fr.umontpellier.iut.autench;

import fr.umontpellier.iut.queryhelper.QueryCode;
import fr.umontpellier.iut.queryhelper.QueryType;
import fr.umontpellier.iut.queryhelper.SignedQuery;

public class SendKeyQuery extends SignedQuery {
    private final String cipherKey;

    protected SendKeyQuery(QueryType type, QueryCode code, String action, String signature, String encodedKey,
                        String cipherKey) {
        super(type, code, action, signature, encodedKey);
        this.cipherKey = cipherKey;
    }

    public SendKeyQuery(String signature, String encodedKey,
                        String cipherKey) {
        this(QueryType.SINGLE, QueryCode.SUCCESS, "sendKey", signature,  encodedKey, cipherKey);
    }

    public String getCipherKey() {
        return cipherKey;
    }

    @Override
    public String getSignedMessage() {
        return cipherKey;
    }
}
