package io.stealingdapenta.mc2048.commands;

import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_NOT_PLAYER;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_TOP_SELF;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_TOP_TEN;

import io.stealingdapenta.mc2048.utils.HighScoreManager;
import io.stealingdapenta.mc2048.utils.MessageSender;
import io.stealingdapenta.mc2048.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalInt;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HighScoreCommand implements CommandExecutor {

    private static final String PLAYER_SCORE = "%d. %s: ";

    private final HighScoreManager highScoreManager;
    private final MessageSender messageSender;

    public HighScoreCommand(HighScoreManager highScoreManager, MessageSender messageSender) {
        this.highScoreManager = highScoreManager;
        this.messageSender = messageSender;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {

            messageSender.sendMessage(sender, MSG_NOT_PLAYER);
            return true;
        }
        OptionalInt playersPosition = highScoreManager.getPlayerPosition(player);
        Map<String, Integer> allHighScores = highScoreManager.getHighScores();
        Map<String, Integer> highScores = highScoreManager.getTop10HiScores(allHighScores);

        messageSender.sendMessage(player, MSG_TOP_TEN);
        List<Entry<String, Integer>> highScoresList = new ArrayList<>(highScores.entrySet());

        highScoresList.stream()
                      .map(entry -> String.format(PLAYER_SCORE, highScoresList.indexOf(entry) + 1, entry.getKey()) + StringUtil.formatInt(entry.getValue()))
                      .forEach(player::sendMessage);

        String rank = playersPosition.isPresent() ? StringUtil.formatInt(playersPosition.getAsInt()) : "Unranked";
        messageSender.sendMessage(player, MSG_TOP_SELF.getFormattedValue(rank));

        return true;
    }
}