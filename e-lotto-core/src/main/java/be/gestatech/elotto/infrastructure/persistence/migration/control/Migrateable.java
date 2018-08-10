package be.gestatech.elotto.infrastructure.persistence.migration.control;

import org.mongodb.morphia.Datastore;

public interface Migrateable {

    void executeMigration(Datastore datastore) throws Exception;

}
