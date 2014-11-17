package pw.scho.battleship.persistence.configuration;


import com.fasterxml.jackson.databind.MapperFeature;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.jongo.Jongo;
import org.jongo.marshall.jackson.JacksonMapper;

import java.net.UnknownHostException;

public class MongoConfiguration {

    public static Jongo getInstance() {
        return Singleton.INSTANCE.jongo;
    }

    private enum Singleton {

        INSTANCE;

        private final Jongo jongo;

        private Singleton() {
            String mongoHgUrl = System.getenv("MONGOHQ_URL");
            DB db = null;

            if (mongoHgUrl != null) {
                try {
                    MongoClientURI mongoClientURI = new MongoClientURI(mongoHgUrl);

                    db = new MongoClient(mongoClientURI).getDB(mongoClientURI.getDatabase());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    db = new MongoClient().getDB("battleship");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }


            jongo = new Jongo(db,
                    new JacksonMapper.Builder()
                            .enable(MapperFeature.AUTO_DETECT_FIELDS)
                            .build()
            );


        }


    }
}
