package io.stealingdapenta.mc2048.config;

import java.util.EnumMap;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Immutable validated configuration used by running games.
 *
 * <p>Reloads build a new snapshot before replacing the live one, so an invalid or partially
 * loaded file cannot change behavior midway through a session.
 */
public final class ConfigurationSnapshot {

    private final Map<ConfigKey, String> values;

    private ConfigurationSnapshot(Map<ConfigKey, String> values) {
        this.values = Map.copyOf(values);
    }

    public static ConfigurationSnapshot from(FileConfiguration configuration) {
        EnumMap<ConfigKey, String> values = new EnumMap<>(ConfigKey.class);
        for (ConfigKey key : ConfigKey.values()) {
            values.put(key, configuration.getString(key.path()));
        }
        return new ConfigurationSnapshot(values);
    }

    static ConfigurationSnapshot from(Map<ConfigKey, String> values) {
        return new ConfigurationSnapshot(new EnumMap<>(values));
    }

    public String get(ConfigKey key) {
        return values.get(key);
    }
}
