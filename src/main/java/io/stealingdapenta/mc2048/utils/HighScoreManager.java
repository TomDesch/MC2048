package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_HIGH_SCORE_LORE_FORMAT;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_HIGH_SCORE_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_HELP_GUI_HIGH_SCORE;
import static io.stealingdapenta.mc2048.utils.FileManager.FILE_MANAGER;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;


public class HighScoreManager {

    private static final String ERROR_FETCHING_FILES = "Something went wrong fetching the player files. Returning empty list.";
    private static final String DOT_YML = ".yml";

    public ItemStack getHighScoresItem() {
        return (new ItemBuilder(MATERIAL_HELP_GUI_HIGH_SCORE.getMaterialValue())).setDisplayName(HELP_GUI_HIGH_SCORE_NAME.getFormattedValue())
                                                                                 .addLoreList(getTopTenLore())
                                                                                 .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                 .create();
    }

    private List<Component> getTopTenLore() {
        final String PLAYER_SCORE = "%d. %s: %d"; // e.g. 3. StealingDaPenta: 45987

        // Get top 10 scores and convert to list for indexed access
        List<Entry<String, Integer>> highScoresList = new ArrayList<>(getTop10HiScores().entrySet());

        List<Component> lore = new ArrayList<>();
        for (int i = 0; i < highScoresList.size(); i++) {
            Entry<String, Integer> entry = highScoresList.get(i);
            String scoreText = String.format(PLAYER_SCORE, i + 1,                  // Position (1-based)
                                             entry.getKey(),         // Player name
                                             entry.getValue()        // Score
                                            );

            lore.add(HELP_GUI_HIGH_SCORE_LORE_FORMAT.getFormattedValue(scoreText));
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
        File userFilesFolder = FILE_MANAGER.getUserFiles();
        File[] playerFiles = userFilesFolder.listFiles();

        if (Objects.isNull(playerFiles) || playerFiles.length == 0) {
            logger.warning(ERROR_FETCHING_FILES);
            return new HashMap<>();
        }

        Map<String, Integer> topHighScores = Arrays.stream(playerFiles)
                                                   .filter(File::isFile)
                                                   .filter(playerFile -> playerFile.getName()
                                                                                   .endsWith(DOT_YML))
                                                   .map(this::getPlayerScorePair)
                                                   .filter(Objects::nonNull)
                                                   .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return sortByHiScores(topHighScores);
    }

    public int getPlayerPosition(Player targetPlayer) {
        List<String> sortedPlayers = getHighScores().entrySet()
                                                    .stream()
                                                    .sorted(Entry.<String, Integer>comparingByValue()
                                                                 .reversed())
                                                    .map(Entry::getKey)
                                                    .toList();

        return sortedPlayers.indexOf(targetPlayer.getName()) + 1;
    }

    public Map<String, Integer> getTop10HiScores() {
        return getHighScores().entrySet()
                              .stream()
                              .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // Sort by value in descending order
                              .limit(10) // Take the top 10
                              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, // merge function in case of duplicates
                                                        LinkedHashMap::new)); // Use LinkedHashMap to maintain insertion order
    }

    private Map.Entry<String, Integer> getPlayerScorePair(File playerFile) {
        String uuid = getPlayerUUIDFrom(playerFile);
        String playerName = getPlayerName(uuid);
        if (Objects.nonNull(playerName)) {
            int hiScore = FILE_MANAGER.getIntByKey(uuid, PlayerConfigField.HIGH_SCORE.getKey());
            return new AbstractMap.SimpleEntry<>(playerName, hiScore);
        }
        return null;
    }

    private String getPlayerName(String uuid) {
        return Bukkit.getOfflinePlayer(UUID.fromString(uuid))
                     .getName();
    }

    private String getPlayerUUIDFrom(File playerFile) {
        return playerFile.getName()
                         .substring(0, playerFile.getName()
                                                 .length() - 4);
    }
}
