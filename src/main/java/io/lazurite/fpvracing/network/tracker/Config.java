package io.lazurite.fpvracing.network.tracker;

import io.lazurite.fpvracing.network.tracker.generic.GenericType;
import io.lazurite.fpvracing.server.entity.flyable.QuadcopterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Properties;

/**
 * The main config handling class for not only reading and writing
 * from the file, but also being passed between the client and
 * the server.
 */
public class Config extends Properties {

    /**
     * Empty constructor :/
     */
    public Config() {

    }

    /**
     * Get a value from the config using a {@link Key} object.
     * @param key the key from the config
     * @param <T> the type of the key
     * @return the value returned from the key
     */
    public <T> T getValue(Key<T> key) {
        return key.getType().fromConfig(this, key.getName());
    }

    /**
     * A class that represents a key in the config file.
     * Stores the name and the type of the key.
     * @param <T> the type of the key
     */
    public static class Key<T> {
        private final String name;
        private final GenericType<T> type;

        public Key(String key, GenericType<T> type) {
            this.name = key;
            this.type = type;
        }

        public String getName() {
            return this.name;
        }

        public GenericType<T> getType() {
            return this.type;
        }
    }
}