package org.mahatma.atp.common.engine;

import org.helium.database.Database;
import org.helium.redis.RedisClient;
import org.mahatma.atp.common.engine.spi.ConfigProvider;
import org.mahatma.atp.common.exception.AutoTestRuntimeException;

public interface ResourcesManager {
    void setConfigProvider(ConfigProvider configProvider);

    Database getDatabaseBydbPro();

    Database getDatabase(String dbName) throws AutoTestRuntimeException;

    RedisClient getRedis(String name);
}
