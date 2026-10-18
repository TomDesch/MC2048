package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.config.ConfigKey.HIGH_SCORE_ITEM_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.HIGH_SCORE_ITEM_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.HIGH_SCORE_ITEM_LORE_FORMAT;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import io.stealingdapenta.mc2048.config.PlayerConfigField;
import io.stealingdapenta.mc2048.persistence.PlayerDataService;


public class HighScoreManager {

    private static final String ERROR_FETCHING_FILES = "Something went wrong fetching the player files. Returning empty list.";
    private static final String DOT_YML = ".yml";
    private static final String PLAYER_NAME_KEY = "Player Name";
    private final Logger logger;
    private volatile Map<UUID, PlayerScore> cachedHighScores = Map.of();

    public HighScoreManager(Logger logger) {
        this.logger = Objects.requireNonNull(logger, "logger");
    }

    public ItemStack getHighScoresItem() {
        return (new ItemBuilder(HIGH_SCORE_ITEM_MATERIAL.getMaterialValue())).setDisplayName(HIGH_SCORE_ITEM_NAME.getFormattedValue())
                                                                                 .addLoreList(getTopTenLore())
                                                                                 .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                 .create();
    }

    private List<Component> getTopTenLore() {
        final String PLAYER_SCORE = "%d. %s: " + StringUtil.translate("&f"); // e.g. 3. StealingDaPenta:

        // Get top 10 scores and convert to list for indexed access
        List<Entry<String, Integer>> highScoresList = new ArrayList<>(getTop10HiScores().entrySet());

        List<Component> lore = new ArrayList<>();
        for (int i = 0; i < highScoresList.size(); i++) {
            Entry<String, Integer> entry = highScoresList.get(i);
            String scoreText = String.format(PLAYER_SCORE, i + 1,                   // Position (1-based)
                                             entry.getKey()                         // Player name
                                            );
            scoreText = scoreText + StringUtil.formatInt(entry.getValue());         // Score

            lore.add(HIGH_SCORE_ITEM_LORE_FORMAT.getFormattedValue(scoreText));
        }
        return lore;
    }


    private Map<String, Integer> sortByHiScores(Map<String, Integer> highScores) {
        return highScores.entrySet()
                         .stream()
                         .sorted(Map.Entry.<String, Integer>comparingByValue()
                                          .reversed())
                         .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
    }

    public Map<String, Integer> getHighScores() {
        Map<String, Integer> scoresByName = cachedHighScores.values()
                                                            .stream()
                                                            .collect(Collectors.toMap(
                                                                PlayerScore::playerName,
                                                                PlayerScore::score,
                                                                Math::max
                                                            ));
        return Collections.unmodifiableMap(sortByHiScores(scoresByName));
    }

    public void refreshAsync(PlayerDataService playerDataService) {
        playerDataService.loadAll()
                         .thenAccept(batch -> {
                             Map<UUID, PlayerScore> loaded = batch.players().entrySet().stream().collect(
                                 Collectors.toMap(
                                     Map.Entry::getKey,
                                     entry -> new PlayerScore(
                                         entry.getValue().playerName(),
                                         entry.getValue().highScore()
                                     )
                                 )
                             );
                             mergeIntoCache(loaded);
                             batch.failures().forEach(failure ->
                                 logWarning("Skipping malformed player data file " + failure));
                         })
                         .exceptionally(exception -> {
                             logWarning(ERROR_FETCHING_FILES);
                             logWarning(exception.getMessage());
                             return null;
                         });
    }

    public void recordScore(Player player, int score) {
        recordScore(player.getUniqueId(), player.getName(), score);
    }

    public synchronized void recordScore(UUID playerId, String playerName, int score) {
        Map<UUID, PlayerScore> updatedScores = new HashMap<>(cachedHighScores);
        updatedScores.merge(
            playerId,
            new PlayerScore(playerName, score),
            (existing, updated) -> new PlayerScore(updated.playerName(), Math.max(existing.score(), updated.score()))
        );
        cachedHighScores = Collections.unmodifiableMap(updatedScores);
    }

    private synchronized void mergeIntoCache(Map<UUID, PlayerScore> loadedScores) {
        Map<UUID, PlayerScore> mergedScores = new HashMap<>(loadedScores);
        cachedHighScores.forEach((playerId, current) -> mergedScores.merge(
            playerId,
            current,
            (loaded, cached) -> new PlayerScore(cached.playerName(), Math.max(loaded.score(), cached.score()))
        ));
        cachedHighScores = Collections.unmodifiableMap(mergedScores);
    }

    private Map<UUID, PlayerScore> loadHighScores(File userFilesFolder) {
        File[] playerFiles = userFilesFolder.listFiles();

        if (Objects.isNull(playerFiles) || playerFiles.length == 0) {
            return new HashMap<>();
        }

        Map<UUID, PlayerScore> loadedScores = new HashMap<>();
        Arrays.stream(playerFiles)
              .filter(File::isFile)
              .filter(playerFile -> playerFile.getName().endsWith(DOT_YML))
              .forEach(playerFile -> loadPlayerScore(playerFile).ifPresent(
                  entry -> loadedScores.put(entry.getKey(), entry.getValue())));
        return loadedScores;
    }

    public OptionalInt getPlayerPosition(Player targetPlayer) {
        return getPlayerPosition(targetPlayer.getUniqueId(), targetPlayer.getName());
    }

    OptionalInt getPlayerPosition(UUID playerId, String playerName) {
        PlayerScore currentScore = cachedHighScores.get(playerId);
        if (currentScore != null && !currentScore.playerName().equals(playerName)) {
            recordScore(playerId, playerName, currentScore.score());
        }

        List<UUID> sortedPlayers = cachedHighScores.entrySet()
                                                   .stream()
                                                   .sorted(Entry.<UUID, PlayerScore>comparingByValue(
                                                      Comparator.comparingInt(PlayerScore::score)).reversed())
                                                   .map(Entry::getKey)
                                                   .toList();

        int index = sortedPlayers.indexOf(playerId);
        return index < 0 ? OptionalInt.empty() : OptionalInt.of(index + 1);
    }

    public Map<String, Integer> getTop10HiScores() {
        return getTop10HiScores(getHighScores());
    }

    public Map<String, Integer> getTop10HiScores(Map<String, Integer> highScores) {
        return highScores.entrySet()
                              .stream()
                              .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // Sort by value in descending order
                              .limit(10) // Take the top 10
                              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, // merge function in case of duplicates
                                                        LinkedHashMap::new)); // Use LinkedHashMap to maintain insertion order
    }

    void refresh(File userFilesFolder) {
        mergeIntoCache(loadHighScores(userFilesFolder));
    }

    private Optional<Map.Entry<UUID, PlayerScore>> loadPlayerScore(File playerFile) {
        String fileId = getPlayerUUIDFrom(playerFile);
        try {
            UUID playerId = UUID.fromString(fileId);
            YamlConfiguration configuration = new YamlConfiguration();
            configuration.load(playerFile);
            String playerName = configuration.getString(PLAYER_NAME_KEY);
            if (playerName == null || playerName.isBlank()) {
                logWarning("Skipping player data without a player name: " + playerFile.getName());
                return Optional.empty();
            }
            int highScore = configuration.getInt(PlayerConfigField.HIGH_SCORE.getKey());
            return Optional.of(Map.entry(playerId, new PlayerScore(playerName, highScore)));
        } catch (IllegalArgumentException | IOException | InvalidConfigurationException exception) {
            logWarning("Skipping malformed player data file " + playerFile.getName() + ": "
                + exception.getMessage());
            return Optional.empty();
        }
    }

    private String getPlayerUUIDFrom(File playerFile) {
        return playerFile.getName()
                         .substring(0, playerFile.getName()
                                                 .length() - 4);
    }

    private void logWarning(String message) {
        logger.warning(message);
    }

    private record PlayerScore(String playerName, int score) {
    }
}
