package be.gestatech.elotto.infrastructure.persistence.migration.version;

import be.gestatech.elotto.infrastructure.persistence.migration.control.Migrateable;
import org.mongodb.morphia.Datastore;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@SuppressWarnings("unused")
public class V_000004 implements Migrateable {


    @Override
    public void executeMigration(Datastore datastore) {
        PriceList priceList = datastore.createQuery(PriceList.class).field("lotteryIdentifier").equal(EuroJackpotLottery.IDENTIFIER).get();
        if (priceList == null) {
            priceList = new PriceList();
        }
        priceList.setLotteryIdentifier(EuroJackpotLottery.IDENTIFIER);
        priceList.setPricePerField(200);
        priceList.setPriceSuper6(125);
        priceList.setPriceSpiel77(250);
        priceList.setPriceGluecksspirale(500);
        priceList.setFeeGluecksspirale(60);
        priceList.setFeeFirstDrawing(60);
        priceList.setFeeSecondDrawing(40);
        priceList.setValidFrom(getInitialDate());
        datastore.save(priceList);
    }

    ZonedDateTime getInitialDate() {
        return ZonedDateTime.of(2017, 2, 18, 0, 0, 0, 0, ZoneId.of("Europe/Paris"));
    }
}
