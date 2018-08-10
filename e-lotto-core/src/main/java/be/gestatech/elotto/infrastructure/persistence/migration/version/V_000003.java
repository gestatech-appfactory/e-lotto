package be.gestatech.elotto.infrastructure.persistence.migration.version;

import be.gestatech.elotto.infrastructure.persistence.migration.control.Migrateable;
import org.mongodb.morphia.Datastore;

import java.io.IOException;

@SuppressWarnings("unused")
public class V_000003 implements Migrateable {

    @Override
    public void executeMigration(Datastore datastore) throws IOException {
        German6aus49Jackpot german6aus49Jackpot = new German6aus49Jackpot();
        german6aus49Jackpot.setJackpotInMillions(1);

        EuroJackpotJackpot euroJackpotJackpot = new EuroJackpotJackpot();
        euroJackpotJackpot.setJackpotInMillions(1);

        datastore.save(german6aus49Jackpot);
        datastore.save(euroJackpotJackpot);
    }

}
