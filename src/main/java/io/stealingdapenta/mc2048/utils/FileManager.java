package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;

import io.stealingdapenta.mc2048.MC2048;
import io.stealingdapenta.mc2048.config.ConfigKey;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public enum FileManager {

    FILE_MANAGER;

    private static final String EXCEPTION = "Error in file manager.";
    private static final String MC2048_STRING = "mc2048";
    private static final String FILE_CREATED = "MC 2048: YML file created for %s.";
    private static final String FILE_NOT_CREATED = "MC 2048: YML file failed to create for %s.";
    private static final String FILE_SHOULD_EXIST_ERROR = "Error fetching player file by UUID that should exist!";

    public YamlConfiguration getConfig(Player player) {
        return YamlConfiguration.loadConfiguration(getPlayerFile(player));
    }

    public YamlConfiguration getConfig(String uuid) {
        return YamlConfiguration.loadConfiguration(getPlayerFile(uuid));
    }

    public double getDoubleByKey(Player player, String key) {
        return getConfig(player).getDouble(key);
    }

    public int getIntByKey(Player player, String key) {
        return getConfig(player).getInt(key);
    }

    public int getAnimationSpeed(Player player) {
        int savedValue = getConfig(player).getInt("speed", -1);
        if (savedValue<0) return ConfigKey.DEFAULT_ANIMATION_SPEED.getIntValue();
        else return savedValue;
    }

    public int getIntByKey(String uuid, String key) {
        return getConfig(uuid).getInt(key);
    }

    public long getLongByKey(Player player, String key) {
        return getConfig(player).getLong(key);
    }

    public void setValueByKey(Player player, String key, Object value) {
        YamlConfiguration yamlConfiguration = getConfig(player);
        yamlConfiguration.set(key, value);
        saveConfig(player, yamlConfiguration);
    }

    private void saveConfig(Player player, YamlConfiguration config) {
        try {
            config.save(getPlayerFile(player));
        } catch (IOException e) {
            logger.warning(EXCEPTION);
            logger.warning(e.getMessage());
        }
    }

    private String getFileName(Player player) {
        return getFileName(player.getUniqueId()
                                 .toString());
    }

    private String getFileName(String uuid) {
        return uuid + ".yml";
    }

    public void createFile(Player player) {
        File file = new File(getUserFiles(), getFileName(player));
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            logger.warning(EXCEPTION);
            logger.warning(e.getMessage());
        }
        try {
            file.createNewFile();
            writer = new PrintWriter(file);
            writer.println("Player Name: " + player.getName());
            logger.info(FILE_CREATED.formatted(player.getName()));
        } catch (IOException e1) {
            logger.warning(FILE_NOT_CREATED.formatted(player.getName()));
            logger.warning(EXCEPTION);
            logger.warning(e1.getMessage());
        } finally {
            assert writer != null;
            writer.close();
        }
    }

    public File getPlayerFile(Player player) {
        File file = new File(getUserFiles(), getFileName(player));
        if (!file.exists()) {
            createFile(player);
            savePlayerFile(player);
        }
        return file;
    }

    public File getPlayerFile(String uuid) {
        File file = new File(getUserFiles(), getFileName(uuid));
        if (!file.exists()) {
            logger.severe(FILE_SHOULD_EXIST_ERROR);
        }
        return file;
    }

    public File getUserFiles() {
        File userFiles = new File(MC2048.getInstance()
                                        .getDataFolder() + File.separator + MC2048_STRING);
        userFiles.mkdirs();
        return userFiles;
    }

    public void savePlayerFile(Player player) {
        File file = new File(getUserFiles(), getFileName(player));
        if (!file.exists()) {
            createFile(player);
        }
        try {
            YamlConfiguration.loadConfiguration(file)
                             .save(file);
        } catch (IOException e) {
            logger.warning(EXCEPTION);
            logger.warning(e.getMessage());
        }
    }
}