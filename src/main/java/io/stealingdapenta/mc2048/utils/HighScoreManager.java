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
    private volatile Map<String, Integer> cachedHighScores = Map.of();

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
        return cachedHighScores;
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

    public synchronized void recordScore(String playerName, int score) {
        Map<String, Integer> updatedScores = new HashMap<>(cachedHighScores);
        updatedScores.merge(playerName, score, Math::max);
        cachedHighScores = Collections.unmodifiableMap(sortByHiScores(updatedScores));
    }

    private synchronized void mergeIntoCache(Map<String, Integer> loadedScores) {
        Map<String, Integer> mergedScores = new HashMap<>(loadedScores);
        cachedHighScores.forEach((playerName, score) -> mergedScores.merge(playerName, score, Math::max));
        cachedHighScores = Collections.unmodifiableMap(sortByHiScores(mergedScores));
    }

    private Map<String, Integer> loadHighScores() {
        File userFilesFolder = FILE_MANAGER.getUserFiles();
        File[] playerFiles = userFilesFolder.listFiles();

        if (Objects.isNull(playerFiles) || playerFiles.length == 0) {
            return new HashMap<>();
        }

        Map<String, Integer> topHighScores = Arrays.stream(playerFiles)
                                                   .filter(File::isFile)
                                                   .filter(playerFile -> playerFile.getName()
                                                                                   .endsWith(DOT_YML))
                                                   .map(this::getPlayerScorePair)
                                                   .filter(Objects::nonNull)
                                                   .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Math::max));

        return sortByHiScores(topHighScores);
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

    private Map.Entry<String, Integer> getPlayerScorePair(File playerFile) {
        String uuid = getPlayerUUIDFrom(playerFile);
        String playerName = FILE_MANAGER.getStringByKey(uuid, PLAYER_NAME_KEY);
        if (Objects.nonNull(playerName)) {
            int hiScore = FILE_MANAGER.getIntByKey(uuid, PlayerConfigField.HIGH_SCORE.getKey());
            return new AbstractMap.SimpleEntry<>(playerName, hiScore);
        }
        return null;
    }

    private String getPlayerUUIDFrom(File playerFile) {
        return playerFile.getName()
                         .substring(0, playerFile.getName()
                                                 .length() - 4);
    }
}
