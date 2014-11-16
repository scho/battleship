package pw.scho.battleship.persistence.configuration;


import com.mongodb.DB;
import com.mongodb.MongoURI;
import com.mongodb.ServerAddress;
import org.mongolink.MongoSession;
import org.mongolink.MongoSessionManager;
import org.mongolink.Settings;
import org.mongolink.UpdateStrategies;
import org.mongolink.domain.mapper.ContextBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoConfiguration {

    public static void stop() {
        Singleton.INSTANCE.mongoSessionManager.close();
    }

    public static MongoSession createSession() {
        return Singleton.INSTANCE.mongoSessionManager.createSession();
    }

    private enum Singleton {

        INSTANCE;

        private final MongoSessionManager mongoSessionManager;

        private Singleton() {
            ContextBuilder builder = new ContextBuilder("pw.scho.battleship.persistence.mongo.mapping");
            Settings settings = null;
            try {
                settings = Settings.defaultInstance()
                        .withDefaultUpdateStrategy(UpdateStrategies.DIFF)
                        .withDbName("battleship");
                String mongoHqUrl = System.getenv("MONGOHQ_URL");

                if(mongoHqUrl!= null){
                    MongoURI mongoURI = new MongoURI(mongoHqUrl);
                    List<ServerAddress> serverAdresses = new ArrayList<>();
                    for(String host : mongoURI.getHosts()){
                        serverAdresses.add(new ServerAddress(host, 27017));
                    }
                    settings = settings.withAddresses(serverAdresses)
                            .withAuthentication(mongoURI.getUsername(), mongoURI.getPassword().toString());
                } else {
                    settings = settings.withAddresses(Arrays.asList(new ServerAddress("localhost", 27017)));
                }
            } catch (UnknownHostException e) {
            }
            mongoSessionManager = MongoSessionManager.create(builder, settings);
        }
    }
}
