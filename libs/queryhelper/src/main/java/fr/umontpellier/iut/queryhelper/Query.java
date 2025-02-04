package fr.umontpellier.iut.queryhelper;

import java.util.Objects;

public class Query {
   private final QueryType type;
   private final QueryCode code;
   private final String action;

   public Query(QueryType type, QueryCode code, String action) {
      this.type = Objects.requireNonNull(type);
      this.code = Objects.requireNonNull(code);
      this.action = Objects.requireNonNull(action);
   }

   public QueryType getType() {
      return type;
   }

   public QueryCode getCode() {
      return code;
   }

   public String getAction() {
      return action;
   }
}
