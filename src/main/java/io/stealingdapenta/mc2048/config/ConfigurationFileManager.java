package io.stealingdapenta.mc2048.config;

import io.stealingdapenta.mc2048.MC2048;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public enum ConfigurationFileManager {

    CONFIGURATION_FILE_MANAGER;

    public void loadConfig() {
        JavaPlugin plugin = MC2048.getInstance();

        plugin.saveDefaultConfig();
        FileConfiguration configuration = plugin.getConfig();

        // set default configurations
        for (ConfigKey defaultConfig : ConfigKey.values()) {
            configuration.addDefault(defaultConfig.name()
                                                  .toLowerCase(), defaultConfig.getDefaultValue());
        }

        configuration.options()
                     .copyDefaults(true);
        ConfigurationValidator.validate(configuration);
        plugin.saveConfig();
    }

    public void reloadConfig() {
        JavaPlugin plugin = MC2048.getInstance();
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration candidate = YamlConfiguration.loadConfiguration(configFile);
        try (InputStream defaultConfig = Objects.requireNonNull(
            plugin.getResource("config.yml"), "Packaged config.yml is missing")) {
            candidate.setDefaults(YamlConfiguration.loadConfiguration(
                new InputStreamReader(defaultConfig, StandardCharsets.UTF_8)));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load packaged config.yml", exception);
        }
        ConfigurationValidator.validate(candidate);
        plugin.reloadConfig();
    }
}
