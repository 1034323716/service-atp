package org.mahatma.atp.plugin;

import com.feinno.superpojo.util.StringUtils;
import org.helium.redis.RedisClient;
import org.helium.redis.sentinel.CFG_RedisSentinels;
import org.helium.redis.spi.RedisSentinelManager;
import org.mahatma.atp.common.engine.spi.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class RedisPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisPlugin.class);
    private Map<String, RedisClient> redisClientMap = new HashMap<>();


    private ConfigProvider configProvider;

    protected RedisPlugin(ConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    public RedisClient getRedis(String name) {
        if (!redisClientMap.containsKey(name)) {
            synchronized (redisClientMap) {
                if (!redisClientMap.containsKey(name)) {
                    List<CFG_RedisSentinels> configs = new ArrayList<>();

                    if (name.endsWith("*")) {
                        int i = 0;
                        while (++i > 0) {
                            Properties prop = configProvider.getConfig(name + i);
                            if (null == prop) {
                                break;
                            }
                            try {
                                CFG_RedisSentinels cfg = init(prop, i, name);
                                configs.add(cfg);
                            } catch (IOException e) {
                                LOGGER.error("", e);
                                throw new IllegalArgumentException(e.getMessage());
                            }
                        }


                    } else {
                        Properties prop = configProvider.getConfig(name);
                        if (null == prop) {
                            throw new IllegalArgumentException("sentine config not found. path : " + name + ".properties");
                        }
                        try {
                            CFG_RedisSentinels cfg = init(prop, 1, name);
                            configs.add(cfg);
                        } catch (IOException e) {
                            LOGGER.error("", e);
                            throw new IllegalArgumentException(e.getMessage());
                        }
                    }
                    if (configs.isEmpty()) {
                        return null;
                    }
                    redisClientMap.put(name, RedisSentinelManager.INSTANCE.getRedisClient(name, configs));
                }
            }
        }
        return redisClientMap.get(name);
    }


    private CFG_RedisSentinels init(Properties prop, int i, String redisRoleName) throws IOException {

        String enabled = prop.getProperty("enabled");
        if (!StringUtils.isNullOrEmpty(enabled) && !Boolean.valueOf(enabled)) {
            return null;
        }
        CFG_RedisSentinels redisSentinels = new CFG_RedisSentinels();
        redisSentinels.setId(i);
        redisSentinels.setRoleName(redisRoleName);
        String masterName = prop.getProperty("masterName");
        redisSentinels.setMasterName(masterName);
        prop.remove("masterName");
        String policy = prop.getProperty("policy");
        redisSentinels.setPolicy(policy);
        prop.remove("policy");
        String nodeOrder = prop.getProperty("nodeOrder");
        redisSentinels.setNodeOrder(Integer.valueOf(nodeOrder));
        prop.remove("nodeOrder");
        String server = prop.getProperty("server");
        redisSentinels.setAddrs(server);
        prop.remove("server");
        String weight = prop.getProperty("weight");
        prop.remove("weight");
        redisSentinels.setWeight(Integer.valueOf(weight));
        redisSentinels.setEnabled(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        prop.store(out, "");

        redisSentinels.setPropertiesExt(out.toString());

        out.close();
        return redisSentinels;
    }
}
