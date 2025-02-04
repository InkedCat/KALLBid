package fr.umontpellier.iut.queryhelper;

import com.google.gson.Gson;

public class QuerySerializer<T extends Query> {
    private final Gson json;

    public QuerySerializer(Gson json) {
        this.json = json;
    }

    public QuerySerializer() {
        this(new Gson());
    }

    public String serialize(T query) {
        return json.toJson(query);
    }
}
