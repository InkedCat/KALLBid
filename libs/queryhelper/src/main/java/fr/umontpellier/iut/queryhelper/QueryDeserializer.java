package fr.umontpellier.iut.queryhelper;

import com.google.gson.Gson;

public class QueryDeserializer<T extends Query> {
    private final Gson json;

    private final Class<T> queryClass;

    public QueryDeserializer(Gson json, Class<T> queryClass) {
        this.json = json;
        this.queryClass = queryClass;
    }

    public QueryDeserializer(Class<T> queryClass) {
        this(new Gson(), queryClass);
    }

    public T deserialize(String query) {
        return json.fromJson(query, queryClass);
    }
}
