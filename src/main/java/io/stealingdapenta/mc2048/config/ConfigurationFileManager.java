package io.stealingdapenta.mc2048.config;

import io.stealingdapenta.mc2048.MC2048;
import java.util.Objects;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationFileManager {

    private static ConfigurationFileManager instance;

    private ConfigurationFileManager() {
    }

    public static ConfigurationFileManager getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ConfigurationFileManager();
        }
        return instance;
    }

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
        plugin.saveConfig();
    }

    public void reloadConfig() {
        JavaPlugin plugin = MC2048.getInstance();
        plugin.reloadConfig();
    }
}
