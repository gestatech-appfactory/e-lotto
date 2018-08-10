package be.gestatech.elotto.infrastructure.persistence;

import be.gestatech.elotto.infrastructure.persistence.encryption.EncryptedFieldConverter;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Singleton
@Startup
public class DBConnection {

    private static MongoClient mongoClient;   // is thread-safe because of @Singleton
    private static Datastore datastore; // is thread-safe because of @Singleton

    public static final String DATA_BASE_NAME = "elotto_db";

    public DBConnection() {
    }

    @PostConstruct
    public void init() {
        if (Objects.isNull(mongoClient ) && Objects.isNull(datastore)) {
            mongoClient = initializeMongoDbConnection();
            Morphia morphia = new Morphia();
            datastore = morphia.createDatastore(mongoClient, DATA_BASE_NAME);
            morphia.getMapper().getOptions().setMapSubPackages(true);
            morphia.getMapper().getOptions().setStoreEmpties(true);
            morphia.mapPackage("be.gestatech", true);
            morphia.getMapper().getConverters().addConverter(ZonedDateTimeUTCtoEuropeConverter.class);
            morphia.getMapper().getConverters().addConverter(CurrencyConverter.class);
            morphia.getMapper().getConverters().addConverter(EncryptedFieldConverter.class);
            datastore.ensureIndexes(true);
        }
    }

    private MongoClient initializeMongoDbConnection() {
        String dataBaseServerName = System.getenv("E_LOTTO_DATA_BASE_SERVER_NAME");
        if (Objects.nonNull(dataBaseServerName)) {
            int dataBaseServerPort = Integer.parseInt(System.getenv("E_LOTTO_DATA_BASE_SERVER_PORT"));
            List<MongoCredential> credentials = new ArrayList<>();
            return new MongoClient(new ServerAddress(dataBaseServerName, dataBaseServerPort), credentials);
        } else {
            MongoClientURI uri = new MongoClientURI("mongodb://"
                    + System.getenv("E_LOTOO_MONGO_DATA_BASE_USER") + ":"
                    + System.getenv("E_LOTOO_MONGO_DATA_BASE_PASSWORD")
                    + "@lor-cluster-shard-00-00-us2rg.mongodb.net:27017,lor-cluster-shard-00-01-us2rg.mongodb.net:27017,lor-cluster-shard-00-02-us2rg.mongodb.net:27017/"
                    + DATA_BASE_NAME + "?ssl=true&replicaSet=LOR-Cluster-shard-0&authSource=admin");
            return new MongoClient(uri);
        }
    }

    @Produces
    @RequestScoped
    public Datastore getDatastore() {
        return datastore;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoClient.getDatabase(DATA_BASE_NAME);
    }
}
