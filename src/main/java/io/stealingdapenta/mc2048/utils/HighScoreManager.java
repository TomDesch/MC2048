package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;
import static io.stealingdapenta.mc2048.utils.FileManager.FILE_MANAGER;

import java.io.File;
import java.util.AbstractMap;
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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class HighScoreManager {

    private static final String ERROR_FETCHING_FILES = "Something went wrong fetching the player files. Returning empty list.";
    private static final String DOT_YML = ".yml";

    private static Map<String, Integer> sortByHiScores(Map<String, Integer> highScores) {
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
