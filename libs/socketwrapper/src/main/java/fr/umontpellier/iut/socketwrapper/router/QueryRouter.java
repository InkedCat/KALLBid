package fr.umontpellier.iut.socketwrapper.router;

import com.google.gson.JsonSyntaxException;
import fr.umontpellier.iut.queryhelper.Query;
import fr.umontpellier.iut.queryhelper.QueryDeserializer;
import fr.umontpellier.iut.queryhelper.QueryType;
import fr.umontpellier.iut.socketwrapper.ClientHandler;
import fr.umontpellier.iut.socketwrapper.ClientSocketWrapper;
import fr.umontpellier.iut.socketwrapper.exceptions.SocketClosedException;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class QueryRouter extends ClientHandler {
    private QueryDeserializer<Query> defaultDeserializer;

    private EventManager eventManager;

    public QueryRouter(ClientSocketWrapper client, QueryDeserializer<Query> defaultDeserializer,
                       EventManager eventManager) {
        super(client);
        this.defaultDeserializer = Objects.requireNonNull(defaultDeserializer);
        this.eventManager = Objects.requireNonNull(eventManager);
    }

    public QueryRouter(ClientSocketWrapper client, EventManager eventManager) {
        this(client, new QueryDeserializer<>(Query.class), eventManager);
    }

    @Override
    public void loop() {
        try {
            String jsonQuery = getClient().read();

            Query query = defaultDeserializer.deserialize(jsonQuery);

            Optional<String> response = eventManager.handle(query.getAction(), jsonQuery);

            if(response.isPresent()) {
                getClient().send(response.get());
            }
        }  catch (JsonSyntaxException | IOException | SocketClosedException ignore) {}
    }
}
