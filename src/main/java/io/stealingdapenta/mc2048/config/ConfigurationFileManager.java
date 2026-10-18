package io.stealingdapenta.mc2048.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public enum ConfigurationFileManager {

    CONFIGURATION_FILE_MANAGER;

    private static final List<ConfigKey> CONTROL_SLOT_KEYS = List.of(
        ConfigKey.MOVE_BUTTON_UP_SLOT,
        ConfigKey.MOVE_BUTTON_DOWN_SLOT,
        ConfigKey.MOVE_BUTTON_LEFT_SLOT,
        ConfigKey.MOVE_BUTTON_RIGHT_SLOT,
        ConfigKey.UNDO_BUTTON_SLOT,
        ConfigKey.SPEED_BUTTON_SLOT,
        ConfigKey.RESET_BUTTON_SLOT,
        ConfigKey.PLAYER_ITEM_SLOT
    );

    private volatile ConfigurationSnapshot snapshot;

    public void loadConfig(JavaPlugin plugin) {
        plugin.saveDefaultConfig();

        YamlConfiguration defaults = loadPackagedDefaults(plugin);
        YamlConfiguration candidate = loadCandidate(plugin, defaults);
        try {
            ConfigurationValidator.validate(candidate);
            snapshot = ConfigurationSnapshot.from(candidate);
        } catch (ConfigurationValidationException exception) {
            warnInvalidKeys(plugin, exception);
            ConfigurationValidator.validate(defaults);
            snapshot = ConfigurationSnapshot.from(defaults);
            plugin.getLogger().warning("Using packaged MC2048 defaults without rewriting config.yml.");
        }
    }

    public void reloadConfig(JavaPlugin plugin, boolean gamesActive) {
        YamlConfiguration defaults = loadPackagedDefaults(plugin);
        YamlConfiguration candidate = loadCandidate(plugin, defaults);
        ConfigurationSnapshot candidateSnapshot;
        try {
            ConfigurationValidator.validate(candidate);
            candidateSnapshot = ConfigurationSnapshot.from(candidate);
        } catch (ConfigurationValidationException exception) {
            warnInvalidKeys(plugin, exception);
            throw exception;
        }

        ConfigurationSnapshot current = getSnapshot();
        validateReload(current, candidateSnapshot, gamesActive);
        snapshot = candidateSnapshot;
    }

    public ConfigurationSnapshot getSnapshot() {
        ConfigurationSnapshot current = snapshot;
        if (current == null) {
            throw new IllegalStateException("MC2048 configuration has not been loaded.");
        }
        return current;
    }

    static boolean hasSlotAffectingChanges(ConfigurationSnapshot current, ConfigurationSnapshot candidate) {
        return CONTROL_SLOT_KEYS.stream().anyMatch(key -> !Objects.equals(current.get(key), candidate.get(key)));
    }

    static void validateReload(ConfigurationSnapshot current, ConfigurationSnapshot candidate, boolean gamesActive) {
        if (gamesActive && hasSlotAffectingChanges(current, candidate)) {
            throw new IllegalStateException("Control slot changes require all active games to be closed.");
        }
    }

    private YamlConfiguration loadCandidate(JavaPlugin plugin, YamlConfiguration defaults) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration candidate = YamlConfiguration.loadConfiguration(configFile);
        candidate.setDefaults(defaults);
        return candidate;
    }

    private YamlConfiguration loadPackagedDefaults(JavaPlugin plugin) {
        try (InputStream input = Objects.requireNonNull(
            plugin.getResource("config.yml"), "Packaged config.yml is missing");
             InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            return YamlConfiguration.loadConfiguration(reader);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load packaged config.yml", exception);
        }
    }

    private void warnInvalidKeys(JavaPlugin plugin, ConfigurationValidationException exception) {
        exception.getErrors().forEach((key, message) ->
            plugin.getLogger().warning("Invalid config key " + key.path() + ": " + message));
    }
}
