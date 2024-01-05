package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;

import io.stealingdapenta.mc2048.MC2048;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class FileManager {

    private static final String EXCEPTION = "Error in file manager.";
    private static final String MC2048_STRING = "mc2048";
    private static FileManager fileManager;

    public static FileManager getInstance() {
        if (fileManager == null) {
            fileManager = new FileManager();
        }
        return fileManager;
    }

    public YamlConfiguration getConfig(Player player) {
        return YamlConfiguration.loadConfiguration(getPlayerFile(player));
    }

    public double getDoubleByKey(Player player, String key) {
        return getConfig(player).getDouble(key);
    }

    public int getIntByKey(Player player, String key) {
        return getConfig(player).getInt(key);
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
        return player.getUniqueId() + ".yml";
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
            logger.info("MC 2048: YML file created for %s.".formatted(player.getName()));
        } catch (IOException e1) {
            logger.warning("MC 2048: YML file failed to create for %s.".formatted(player.getName()));
            logger.warning(EXCEPTION);
            logger.warning(e1.getMessage());
        } finally {
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

    public File getUserFiles() {
        File userFiles = new File(MC2048.getInstance()
                                        .getDataFolder() + File.separator + MC2048_STRING);
        if (!userFiles.exists()) {
            userFiles.mkdirs();
        }
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