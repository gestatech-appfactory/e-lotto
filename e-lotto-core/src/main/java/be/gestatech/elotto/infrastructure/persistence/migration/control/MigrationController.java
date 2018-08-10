package be.gestatech.elotto.infrastructure.persistence.migration.control;

import org.mongodb.morphia.Datastore;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

@Singleton
@Startup
@DependsOn("DBConnection")
@SuppressWarnings({"unused", "WeakerAccess"})
public class MigrationController {

    @Inject
    Datastore datastore;

    @PostConstruct
    private void startupAutomatically() {
        int counter = 0;
        boolean migrationExists = true;
        while (migrationExists) {
            final String migrationVersion = "V_" + String.format("%06d", counter);
            final String className = getBasePackageName() + "." + migrationVersion;
            try {
                final Class<?> migrationClass = Class.forName(className);
                final Migrateable migratable = (Migrateable) migrationClass.newInstance();
                final Migration persistentMigration = getExistingPersistentMigration(migrationVersion);
                if (Objects.nonNull(persistentMigration) && persistentMigration.isSuccess()) {
                    ++counter;
                } else {
                    if (Objects.nonNull(persistentMigration)) {
                        executeMigration(migratable, persistentMigration);
                    } else {
                        Migration migration = new Migration(migrationVersion);
                        executeMigration(migratable, migration);
                    }
                    ++counter;
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
                migrationExists = false;
            }
        }
    }

    String getBasePackageName() {
        return V_000000.class.getPackage().getName();
    }

    private Migration getExistingPersistentMigration(String migrationVersion) {
        return datastore.createQuery(Migration.class).field("migrationVersion").equal(migrationVersion).get();
    }

    private void executeMigration(Migrateable migratable, Migration migration) {
        try {
            migratable.executeMigration(datastore);
            migration.setSuccess(true);
            migration.setErrorStacktrace(null);
        } catch (Exception ex) {
            migration.setSuccess(false);
            migration.setErrorStacktrace(getStacktraceAsString(ex));
        }
        datastore.save(migration);
    }

    private String getStacktraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
