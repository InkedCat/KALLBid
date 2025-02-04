package fr.umontpellier.iut.queryhelper;

import java.util.Optional;

public interface QueryHandler<T extends Query> {
    Optional<String> handle(T query);
}
