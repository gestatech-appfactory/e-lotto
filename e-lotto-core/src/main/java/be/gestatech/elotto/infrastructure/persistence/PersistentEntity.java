package be.gestatech.elotto.infrastructure.persistence;

import org.bson.types.ObjectId;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Transient;
import org.mongodb.morphia.annotations.Version;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public abstract class PersistentEntity implements Serializable {

    private static final long serialVersionUID = 523125070709974593L;

    private static DateTimeService dateTimeService = new DateTimeService();

    @Id
    private ObjectId id;

    @Version
    @DiffIgnore
    private Long version;

    @DiffIgnore
    @ZonedDateTimeEurope
    private ZonedDateTime created;

    @DiffIgnore
    @ZonedDateTimeEurope
    private ZonedDateTime lastUpdate;

    @Transient
    private boolean omitValidation;


    @PrePersist
    protected void prePersist() {
        if (Objects.isNull(created)) {
            created = dateTimeService.getDateTimeNowEurope();
        }
        this.lastUpdate = dateTimeService.getDateTimeNowEurope();
        validate();
    }

    protected void validate() {
    }


    public ObjectId getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    @XmlTransient
    public boolean isOmitValidation() {
        return omitValidation;
    }

    public void setOmitValidation(boolean omitValidation) {
        this.omitValidation = omitValidation;
    }

    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        if (Objects.equals(this,o)) {
            isEqual = true;
        } else if (!(o instanceof PersistentEntity)) {
            isEqual = false;
        } else {
            PersistentEntity that = (PersistentEntity) o;
            isEqual = Objects.equals(id, that.id);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PersistentEntity{");
        sb.append("id=").append(id);
        sb.append(", version=").append(version);
        sb.append(", created=").append(created);
        sb.append(", lastUpdate=").append(lastUpdate);
        sb.append(", omitValidation=").append(omitValidation);
        sb.append('}');
        return sb.toString();
    }
}
