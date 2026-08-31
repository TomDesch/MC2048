package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;

import io.stealingdapenta.mc2048.MC2048;
import io.stealingdapenta.mc2048.config.ConfigKey;
import io.stealingdapenta.mc2048.config.PlayerConfigField;
import io.stealingdapenta.mc2048.utils.data.SavedGame;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public enum FileManager {

    FILE_MANAGER;

    private static final String EXCEPTION = "Error in file manager.";
    private static final String MC2048_STRING = "mc2048";
    private static final String FILE_CREATED = "MC 2048: YML file created for %s.";
    private static final String FILE_NOT_CREATED = "MC 2048: YML file failed to create for %s.";
    private static final String FILE_SHOULD_EXIST_ERROR = "Error fetching player file by UUID that should exist!";
    private static final String ACTIVE_GAME_PATH = "active-game";

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
        YamlConfiguration configuration = getConfig(player);
        int savedValue = configuration.getInt(PlayerConfigField.ANIMATION_SPEED.getKey(), -1);
        if (savedValue >= 1 && savedValue <= 6) {
            return savedValue;
        }

        int legacyDelay = configuration.getInt("speed", -1);
        if (legacyDelay >= 0 && legacyDelay <= 5) {
            int migratedSpeed = 6 - legacyDelay;
            configuration.set(PlayerConfigField.ANIMATION_SPEED.getKey(), migratedSpeed);
            configuration.set("speed", null);
            saveConfig(player, configuration);
            return migratedSpeed;
        }

        return ConfigKey.SPEED_BUTTON_SPEED_DEFAULT.getIntValue();
    }

    public int getAnimationDelay(Player player) {
        return 6 - getAnimationSpeed(player);
    }

    public Optional<SavedGame> getSavedGame(Player player) {
        YamlConfiguration configuration = getConfig(player);
        ConfigurationSection section = configuration.getConfigurationSection(ACTIVE_GAME_PATH);
        if (section == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(SavedGame.from(section));
        } catch (IllegalArgumentException exception) {
            logger.warning("Discarding invalid saved game for %s: %s".formatted(player.getName(), exception.getMessage()));
            configuration.set(ACTIVE_GAME_PATH, null);
            saveConfig(player, configuration);
            return Optional.empty();
        }
    }

    public void saveGame(Player player, SavedGame savedGame) {
        YamlConfiguration configuration = getConfig(player);
        configuration.set(ACTIVE_GAME_PATH, null);
        savedGame.writeTo(configuration.createSection(ACTIVE_GAME_PATH));
        saveConfig(player, configuration);
    }

    public void clearSavedGame(Player player) {
        YamlConfiguration configuration = getConfig(player);
        configuration.set(ACTIVE_GAME_PATH, null);
        saveConfig(player, configuration);
    }

    public int getIntByKey(String uuid, String key) {
        return getConfig(uuid).getInt(key);
    }

    public String getStringByKey(String uuid, String key) {
        return getConfig(uuid).getString(key);
    }

    public long getLongByKey(Player player, String key) {
        return getConfig(player).getLong(key);
    }

    public void setValueByKey(Player player, String key, Object value) {
        YamlConfiguration yamlConfiguration = getConfig(player);
        yamlConfiguration.set(key, value);
        saveConfig(player, yamlConfiguration);
    }

    public void updatePlayerName(Player player) {
        YamlConfiguration configuration = getConfig(player);
        if (!Objects.equals(configuration.getString("Player Name"), player.getName())) {
            configuration.set("Player Name", player.getName());
            saveConfig(player, configuration);
        }
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
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(
            file.toPath(), StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW))) {
            writer.println("Player Name: " + player.getName());
            logger.info(FILE_CREATED.formatted(player.getName()));
        } catch (IOException exception) {
            logger.warning(FILE_NOT_CREATED.formatted(player.getName()));
            logger.warning(EXCEPTION);
            logger.warning(exception.getMessage());
            throw new IllegalStateException(FILE_NOT_CREATED.formatted(player.getName()), exception);
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
        if (!userFiles.isDirectory() && !userFiles.mkdirs()) {
            throw new IllegalStateException("Unable to create player data directory: " + userFiles);
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