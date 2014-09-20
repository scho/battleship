package pw.scho.battleship.core.persistence.configuration;


import com.google.common.collect.Lists;
import com.mongodb.ServerAddress;
import org.mongolink.MongoSession;
import org.mongolink.MongoSessionManager;
import org.mongolink.Settings;
import org.mongolink.UpdateStrategies;
import org.mongolink.domain.mapper.ContextBuilder;

import java.net.UnknownHostException;

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
            ContextBuilder builder = new ContextBuilder("pw.scho.battleship.core.persistence.mapping");
            Settings settings = null;
            try {
                settings = Settings.defaultInstance()
                        .withDefaultUpdateStrategy(UpdateStrategies.DIFF)
                        .withDbName("battleship")
                        .withAddresses(Lists.newArrayList(new ServerAddress("localhost", 7689)));

            } catch (UnknownHostException e) {
            }
            mongoSessionManager = MongoSessionManager.create(builder, settings);

        }
    }
}
