package be.gestatech.elotto.infrastructure.persistence.encryption;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class EncryptedZonedDateTime implements Serializable {

    private static final long serialVersionUID = 6381618008665407798L;


    private ZonedDateTime zonedDateTime;


    public EncryptedZonedDateTime() {
        // Intentionally left blank
    }

    public EncryptedZonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }


    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }
}
