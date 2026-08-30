package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;
import static io.stealingdapenta.mc2048.config.ConfigKey.HIGH_SCORE_ITEM_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.HIGH_SCORE_ITEM_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.HIGH_SCORE_ITEM_LORE_FORMAT;
import static io.stealingdapenta.mc2048.utils.FileManager.FILE_MANAGER;

import java.io.File;
import java.util.AbstractMap;
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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import io.stealingdapenta.mc2048.config.PlayerConfigField;


public class HighScoreManager {

    private static final String ERROR_FETCHING_FILES = "Something went wrong fetching the player files. Returning empty list.";
    private static final String DOT_YML = ".yml";
    private static final String PLAYER_NAME_KEY = "Player Name";
    private volatile Map<UUID, PlayerScore> cachedHighScores = Map.of();

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

    public void refreshAsync() {
        CompletableFuture.supplyAsync(this::loadHighScores)
                         .thenAccept(this::mergeIntoCache)
                         .exceptionally(exception -> {
                             logger.warning(ERROR_FETCHING_FILES);
                             logger.warning(exception.getMessage());
                             return null;
                         });
    }

    public void recordScore(Player player, int score) {
        recordScore(player.getUniqueId(), player.getName(), score);
    }

    synchronized void recordScore(UUID playerId, String playerName, int score) {
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

    private Map<UUID, PlayerScore> loadHighScores() {
        File userFilesFolder = FILE_MANAGER.getUserFiles();
        File[] playerFiles = userFilesFolder.listFiles();

        if (Objects.isNull(playerFiles) || playerFiles.length == 0) {
            return new HashMap<>();
        }

        return Arrays.stream(playerFiles)
                                                   .filter(File::isFile)
                                                   .filter(playerFile -> playerFile.getName()
                                                                                   .endsWith(DOT_YML))
                                                   .map(this::getPlayerScorePair)
                                                   .filter(Objects::nonNull)
                                                   .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public int getPlayerPosition(Player targetPlayer) {
        return getPlayerPosition(targetPlayer, getHighScores());
    }

    public int getPlayerPosition(Player targetPlayer, Map<String, Integer> highScores) {
        List<String> sortedPlayers = highScores.entrySet()
                                                    .stream()
                                                    .sorted(Entry.<String, Integer>comparingByValue()
                                                                 .reversed())
                                                    .map(Entry::getKey)
                                                    .toList();

        return sortedPlayers.indexOf(targetPlayer.getName()) + 1;
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

    private Map.Entry<UUID, PlayerScore> getPlayerScorePair(File playerFile) {
        String uuid = getPlayerUUIDFrom(playerFile);
        String playerName = FILE_MANAGER.getStringByKey(uuid, PLAYER_NAME_KEY);
        if (Objects.nonNull(playerName)) {
            int hiScore = FILE_MANAGER.getIntByKey(uuid, PlayerConfigField.HIGH_SCORE.getKey());
            return new AbstractMap.SimpleEntry<>(UUID.fromString(uuid), new PlayerScore(playerName, hiScore));
        }
        return null;
    }

    private String getPlayerUUIDFrom(File playerFile) {
        return playerFile.getName()
                         .substring(0, playerFile.getName()
                                                 .length() - 4);
    }

    private record PlayerScore(String playerName, int score) {
    }
}
