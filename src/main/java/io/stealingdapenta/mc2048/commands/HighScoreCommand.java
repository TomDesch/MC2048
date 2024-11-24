package io.stealingdapenta.mc2048.commands;

import static io.stealingdapenta.mc2048.config.ConfigKey.NOT_PLAYER;
import static io.stealingdapenta.mc2048.config.ConfigKey.PLAYER_POSITION;
import static io.stealingdapenta.mc2048.config.ConfigKey.TOP_TEN;

import io.stealingdapenta.mc2048.utils.HighScoreManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HighScoreCommand implements CommandExecutor {

    private static final String PLAYER_SCORE = "%d. %s: %d";

    private final HighScoreManager highScoreManager;

    public HighScoreCommand(HighScoreManager highScoreManager) {
        this.highScoreManager = highScoreManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            int playersPosition = highScoreManager.getPlayerPosition(player);
            Map<String, Integer> highScores = highScoreManager.getTop10HiScores();

            player.sendMessage(TOP_TEN.getFormattedStringValue());
            List<Entry<String, Integer>> highScoresList = new ArrayList<>(highScores.entrySet());

            highScoresList.stream()
                          .map(entry -> String.format(PLAYER_SCORE, highScoresList.indexOf(entry) + 1, entry.getKey(), entry.getValue()))
                          .forEach(player::sendMessage);

            player.sendMessage(PLAYER_POSITION.getFormattedStringValue() + playersPosition);

            return true;
        }

        sender.sendMessage(NOT_PLAYER.getFormattedStringValue());

        return true;
    }

}