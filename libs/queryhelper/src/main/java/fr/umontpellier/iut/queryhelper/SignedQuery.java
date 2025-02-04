package fr.umontpellier.iut.queryhelper;

public abstract class SignedQuery extends Query {
    private final String signature;
    private final String encodedKey;

    public SignedQuery(QueryType type, QueryCode code, String action, String signature, String encodedKey) {
        super(type, code, action);
        this.signature = signature;
        this.encodedKey = encodedKey;
    }

    public String getSignature() {
        return signature;
    }

    public String getEncodedKey() {
        return encodedKey;
    }

    public abstract String getSignedMessage();
}
