package be.gestatech.elotto.infrastructure.persistence.migration.entity;

import be.gestatech.elotto.infrastructure.persistence.PersistentEntity;
import org.mongodb.morphia.annotations.Entity;

@Entity(value = "migrations", noClassnameStored = true)
public class Migration extends PersistentEntity {

    private String migrationVersion;

    private boolean success;

    private String errorStacktrace;


    public Migration() {
    }

    public Migration(String migrationVersion) {
        this.migrationVersion = migrationVersion;
    }


    public String getMigrationVersion() {
        return migrationVersion;
    }

    public void setMigrationVersion(String migrationVersion) {
        this.migrationVersion = migrationVersion;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorStacktrace() {
        return errorStacktrace;
    }

    public void setErrorStacktrace(String errorStacktrace) {
        this.errorStacktrace = errorStacktrace;
    }
}
