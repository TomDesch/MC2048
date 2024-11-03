package io.stealingdapenta.mc2048.commands;

import io.stealingdapenta.mc2048.utils.HighScoreManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HighScoreCommand implements CommandExecutor {

    private static final String NOT_PLAYER = "You can only execute this command as a player.";
    private static final String TOP_TEN = "Top 10 High Scores on this server:";
    private static final String PLAYER_SCORE = "%d. %s: %d";
    private static final String PLAYER_POSITION = "Your Position: %s";
    private final HighScoreManager highScoreManager;

    public HighScoreCommand(HighScoreManager highScoreManager) {
        this.highScoreManager = highScoreManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            int playersPosition = highScoreManager.getPlayerPosition(player);
            Map<String, Integer> highScores = highScoreManager.getTop10HiScores();

            player.sendMessage(ChatColor.YELLOW + TOP_TEN);
            List<Entry<String, Integer>> highScoresList = new ArrayList<>(highScores.entrySet());

            highScoresList.stream()
                          .map(entry -> String.format(PLAYER_SCORE, highScoresList.indexOf(entry) + 1, entry.getKey(), entry.getValue()))
                          .forEach(player::sendMessage);

            player.sendMessage(ChatColor.YELLOW + PLAYER_POSITION.formatted(playersPosition));

            return true;
        }

        sender.sendMessage(ChatColor.RED + NOT_PLAYER);

        return true;
    }

}