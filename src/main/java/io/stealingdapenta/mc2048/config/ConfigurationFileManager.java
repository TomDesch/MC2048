package io.stealingdapenta.mc2048.config;

import io.stealingdapenta.mc2048.MC2048;
import java.io.File;
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
        ConfigurationValidator.validate(YamlConfiguration.loadConfiguration(configFile));
        plugin.reloadConfig();
    }
}
