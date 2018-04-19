package org.mahatma.atp.plugin;

import org.helium.database.ConnectionString;
import org.helium.database.Database;
import org.helium.database.spi.DatabaseManager;
import org.helium.redis.RedisClient;
import org.mahatma.atp.common.engine.ResourcesManager;
import org.mahatma.atp.common.engine.spi.ConfigProvider;
import org.mahatma.atp.common.exception.AutoTestRuntimeException;
import org.mahatma.atp.common.util.FormatUtil;

import java.util.Properties;

public class ResourcesManagerImpl implements ResourcesManager {
    private ConfigProvider configProvider;

    private Database database = null;


    private RedisPlugin redisPlugin;

    @Override
    public void setConfigProvider(ConfigProvider configProvider) {
        this.configProvider = configProvider;
        redisPlugin = new RedisPlugin(configProvider);
    }

    @Override
    public Database getDatabaseBydbPro() {
        Properties db = configProvider.getConfig(FormatUtil.dbPropertiesName);
        DatabaseManager instance = DatabaseManager.INSTANCE;
        database = instance.getDatabase("", ConnectionString.fromProperties(db));
        return database;
    }

    @Override
    public Database getDatabase(String dbName) throws AutoTestRuntimeException {
        Properties props = configProvider.getConfig(dbName);

        if (props == null) {
            throw new AutoTestRuntimeException("dbName: " + dbName + " not found!");
        }

        DatabaseManager instance = DatabaseManager.INSTANCE;
        database = instance.getDatabase(dbName, ConnectionString.fromProperties(props));
        return database;
    }


    @Override
    public RedisClient getRedis(String name) {
        return redisPlugin.getRedis(name);
    }


}
