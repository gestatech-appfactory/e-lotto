package be.gestatech.elotto.business.activitylog.entity;

import be.gestatech.elotto.infrastructure.persistence.PersistentEntity;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity(value = "activitylog", noClassnameStored = true)
public class ActivityLog extends PersistentEntity {

    private static final long serialVersionUID = 6650274790147153519L;


    private ObjectId playerId;

    @ZonedDateTimeEurope
    private ZonedDateTime timestamp;

    private ActivityType activityType;

    private ActivityFamily activityFamily;

    private Document data;


    @SuppressWarnings({"WeakerAccess"})
    public ActivityLog() {
        // Intentionally left blank
    }

    public ActivityLog(ObjectId playerId, ZonedDateTime timestamp, ActivityType activityType) {
        this.playerId = playerId;
        this.timestamp = timestamp;
        this.activityType = activityType;
        this.activityFamily = activityType.getActivityFamily();
    }


    public ObjectId getPlayerId() {
        return playerId;
    }

    public void setPlayerId(ObjectId playerId) {
        this.playerId = playerId;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public Document getData() {
        return data;
    }

    public void setData(Document data) {
        this.data = data;
    }

    public ActivityFamily getActivityFamily() {
        return activityFamily;
    }

    public void setActivityFamily(ActivityFamily activityFamily) {
        this.activityFamily = activityFamily;
    }

    @Override
    public boolean equals(Object object) {
        if ( Objects.equals(this, object)) return true;
        if (!(object instanceof ActivityLog)) return false;
        if (!super.equals(object)) return false;
        ActivityLog that = (ActivityLog) object;
        return Objects.equals(playerId, that.playerId) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(activityType, that.activityType) &&
                Objects.equals(activityFamily, that.activityFamily) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), playerId, timestamp, activityType, activityFamily, data);
    }
}