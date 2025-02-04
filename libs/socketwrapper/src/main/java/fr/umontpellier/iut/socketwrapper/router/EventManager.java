package fr.umontpellier.iut.socketwrapper.router;

import fr.umontpellier.iut.queryhelper.Query;
import fr.umontpellier.iut.queryhelper.QueryCouple;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EventManager {

    private ConcurrentMap<String, QueryCouple<? extends Query>> events;

    public EventManager(ConcurrentMap<String, QueryCouple<? extends Query>> queries) {
        this.events = queries;
    }

    public EventManager() {
        this.events = new ConcurrentHashMap<>();
    }

    public void addQueryCouple(String action, QueryCouple<? extends Query> queryCouple) {
        events.putIfAbsent(action, queryCouple);
    }

    public void removeQueryCouple(String action) {
        events.remove(action);
    }

    public Optional<String> handle(String action, String jsonQuery) {
        if (!events.containsKey(action)) return Optional.empty();

        return events.get(action).handleJson(jsonQuery);
    }
}
