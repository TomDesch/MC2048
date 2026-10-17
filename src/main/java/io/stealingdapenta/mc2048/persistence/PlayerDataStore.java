package io.stealingdapenta.mc2048.persistence;

import io.stealingdapenta.mc2048.config.PlayerConfigField;
import io.stealingdapenta.mc2048.utils.data.SavedGame;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public final class PlayerDataStore {

    private static final String PLAYER_NAME_KEY = "Player Name";
    private static final String ACTIVE_GAME_PATH = "active-game";

    private final Path dataDirectory;
    private final int defaultAnimationSpeed;
    private final AtomicFileWriter writer;

    public PlayerDataStore(Path dataDirectory, int defaultAnimationSpeed) {
        this(dataDirectory, defaultAnimationSpeed, AtomicFileWriter.system());
    }

    PlayerDataStore(Path dataDirectory, int defaultAnimationSpeed, AtomicFileWriter writer) {
        this.dataDirectory = dataDirectory;
        this.defaultAnimationSpeed = defaultAnimationSpeed;
        this.writer = writer;
    }

    public PlayerData read(UUID playerId, String playerName) throws IOException {
        Path playerFile = playerFile(playerId);
        if (!Files.exists(playerFile)) {
            return defaults(playerId, playerName);
        }

        YamlConfiguration configuration = load(playerFile);
        String storedName = configuration.getString(PLAYER_NAME_KEY, playerName);
        int animationSpeed = readAnimationSpeed(configuration);
        Optional<SavedGame> savedGame = readSavedGame(configuration);
        return new PlayerData(
            playerId,
            storedName,
            configuration.getInt(PlayerConfigField.HIGH_SCORE.getKey()),
            configuration.getInt(PlayerConfigField.ATTEMPTS.getKey()),
            configuration.getLong(PlayerConfigField.PLAYTIME.getKey()),
            configuration.getDouble(PlayerConfigField.AVERAGE_SCORE.getKey()),
            animationSpeed,
            savedGame
        );
    }

    public PlayerData update(UUID playerId, String playerName, UnaryOperator<PlayerData> update) throws IOException {
        PlayerData current = read(playerId, playerName);
        PlayerData updated = update.apply(new PlayerData(
            current.playerId(),
            playerName,
            current.highScore(),
            current.attempts(),
            current.playtime(),
            current.averageScore(),
            current.animationSpeed(),
            current.savedGame()
        ));
        write(updated);
        return updated;
    }

    public PlayerDataBatch readAll() throws IOException {
        Files.createDirectories(dataDirectory);
        Map<UUID, PlayerData> players = new HashMap<>();
        java.util.List<String> failures = new java.util.ArrayList<>();
        try (java.util.stream.Stream<Path> files = Files.list(dataDirectory)) {
            for (Path path : files.filter(Files::isRegularFile).filter(this::isYaml).toList()) {
                String fileName = path.getFileName().toString();
                String rawId = fileName.substring(0, fileName.length() - 4);
                try {
                    UUID playerId = UUID.fromString(rawId);
                    PlayerData data = read(playerId, rawId);
                    players.put(playerId, data);
                } catch (IllegalArgumentException | IOException exception) {
                    failures.add(fileName + ": " + exception.getMessage());
                }
            }
        }
        return new PlayerDataBatch(Map.copyOf(players), List.copyOf(failures));
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    private void write(PlayerData data) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set(PLAYER_NAME_KEY, data.playerName());
        configuration.set(PlayerConfigField.HIGH_SCORE.getKey(), data.highScore());
        configuration.set(PlayerConfigField.ATTEMPTS.getKey(), data.attempts());
        configuration.set(PlayerConfigField.PLAYTIME.getKey(), data.playtime());
        configuration.set(PlayerConfigField.AVERAGE_SCORE.getKey(), data.averageScore());
        configuration.set(PlayerConfigField.ANIMATION_SPEED.getKey(), data.animationSpeed());
        data.savedGame().ifPresent(saved -> saved.writeTo(configuration.createSection(ACTIVE_GAME_PATH)));
        writer.write(playerFile(data.playerId()), configuration.saveToString());
    }

    private YamlConfiguration load(Path playerFile) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(playerFile.toFile());
            return configuration;
        } catch (InvalidConfigurationException exception) {
            throw new IOException("Invalid YAML in " + playerFile.getFileName(), exception);
        }
    }

    private Optional<SavedGame> readSavedGame(YamlConfiguration configuration) {
        ConfigurationSection section = configuration.getConfigurationSection(ACTIVE_GAME_PATH);
        if (section == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(SavedGame.from(section));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    private int readAnimationSpeed(YamlConfiguration configuration) {
        int savedSpeed = configuration.getInt(PlayerConfigField.ANIMATION_SPEED.getKey(), -1);
        if (savedSpeed >= 1 && savedSpeed <= 6) {
            return savedSpeed;
        }
        int legacyDelay = configuration.getInt("speed", -1);
        return legacyDelay >= 0 && legacyDelay <= 5 ? 6 - legacyDelay : defaultAnimationSpeed;
    }

    private PlayerData defaults(UUID playerId, String playerName) {
        return new PlayerData(playerId, playerName, 0, 0, 0, 0, defaultAnimationSpeed, Optional.empty());
    }

    private Path playerFile(UUID playerId) {
        return dataDirectory.resolve(playerId + ".yml");
    }

    private boolean isYaml(Path path) {
        return path.getFileName().toString().endsWith(".yml");
    }

    public record PlayerDataBatch(Map<UUID, PlayerData> players, List<String> failures) {
    }
}
