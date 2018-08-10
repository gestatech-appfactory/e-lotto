package be.gestatech.elotto.business.activitylog.control;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class ActivityLogController implements Serializable {

    private static final long serialVersionUID = 5343432749094938054L;

    private static final Logger logger = Logger.getLogger(ActivityLogController.class.getName());

    public static final int DATA_COMPONENT_SIZE_WITH_KEY_VALUE = 2;

    @Inject
    Datastore datastore;

    @Inject
    DateTimeService dateTimeService;


    @SuppressWarnings({"WeakerAccess"})
    public ActivityLogController() {
    }


    public void saveActivityLog(final Player player, final ActivityType activityType, String... data) {
        saveActivityLog((Objects.nonNull(player) ? player.getId() : null), activityType, data);
    }

    /**
     * The 'data'-parameter contains the key/value-pairs, so the 0./2./4./6. part ist the key, 1./3./5. is the value part.
     */
    public void saveActivityLog(final ObjectId objectId, final ActivityType activityType, String... data) {
        Map<String, String> additionalData = new HashMap<>();
        if (Objects.nonNull(data) && (data.length > 1)) {
            for (int i = 0; i < data.length; i += DATA_COMPONENT_SIZE_WITH_KEY_VALUE) {
                additionalData.put(data[i], data[i + 1]);
            }
        }
        saveActivityLog(objectId, activityType, additionalData);
    }

    private void saveActivityLog(final ObjectId playerId, final ActivityType activityType, final Map<String, String> data) {
        try {
            ActivityLog logEntry = new ActivityLog(playerId, dateTimeService.getDateTimeNowEurope(), activityType);
            if (Objects.nonNull(data) && data.size() > 0) {
                Document dataDocument = new Document();
                data.forEach(dataDocument::append);
                logEntry.setData(dataDocument);
            }
            datastore.save(logEntry); // initial persist
        } catch (Exception ex) {
            // ActivityLog errors MUST NOT break the application flow
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}
