package be.gestatech.elotto.infrastructure.persistence.migration.version;

import be.gestatech.elotto.infrastructure.persistence.migration.control.Migrateable;
import org.mongodb.morphia.Datastore;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@SuppressWarnings("unused")
public class V_000005 implements Migrateable {

    @Override
    public void executeMigration(Datastore datastore) {
        PriceList priceList = datastore.createQuery(PriceList.class).field("lotteryIdentifier").equal(KenoLottery.IDENTIFIER).get();
        if (priceList == null) {
            priceList = new PriceList();
        }
        priceList.setLotteryIdentifier(KenoLottery.IDENTIFIER);
        priceList.setPricePlus5(75);
        priceList.setFeeFirstDrawing(60);
        priceList.setValidFrom(ZonedDateTime.of(2017, 2, 18, 0, 0, 0, 0, ZoneId.of("Europe/Paris")));
        datastore.save(priceList);
    }

}
