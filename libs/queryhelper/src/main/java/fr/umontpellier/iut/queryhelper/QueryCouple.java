package fr.umontpellier.iut.queryhelper;

import java.util.Objects;
import java.util.Optional;

public class QueryCouple<T extends Query> {

    private final QuerySerializer<T> serializer;

    private final QueryDeserializer<T> deserializer;

    private final QueryHandler<T> handler;

    public QueryCouple(Class<T> type, QueryHandler<T> handler) {
        this.serializer = new QuerySerializer<>();
        this.deserializer = new QueryDeserializer<>(type);
        this.handler = Objects.requireNonNull(handler);
    }

    public String serialize(T query) {
        return serializer.serialize(query);
    }

    public T deserialize(String json) {
        return deserializer.deserialize(json);
    }

    public Optional<String> handle(T query) {
        return handler.handle(query);
    }

    public Optional<String> handleJson(String jsonQuery) {
        return handle(deserialize(jsonQuery));
    }
}
