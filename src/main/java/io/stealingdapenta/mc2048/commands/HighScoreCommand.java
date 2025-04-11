package io.stealingdapenta.mc2048.commands;

import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_NOT_PLAYER;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_TOP_SELF;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_TOP_TEN;
import static io.stealingdapenta.mc2048.utils.MessageSender.MESSAGE_SENDER;

import io.stealingdapenta.mc2048.utils.HighScoreManager;
import io.stealingdapenta.mc2048.utils.StringUtil;

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

    private static final String PLAYER_SCORE = "%d. %s: ";

    private final HighScoreManager highScoreManager;

    public HighScoreCommand(HighScoreManager highScoreManager) {
        this.highScoreManager = highScoreManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {

            MESSAGE_SENDER.sendMessage(sender, MSG_NOT_PLAYER);
            return true;
        }
        int playersPosition = highScoreManager.getPlayerPosition(player);
        Map<String, Integer> highScores = highScoreManager.getTop10HiScores();

        MESSAGE_SENDER.sendMessage(player, MSG_TOP_TEN);
        List<Entry<String, Integer>> highScoresList = new ArrayList<>(highScores.entrySet());

        highScoresList.stream()
                      .map(entry -> String.format(PLAYER_SCORE, highScoresList.indexOf(entry) + 1, entry.getKey()) + StringUtil.formatInt(entry.getValue()))
                      .forEach(player::sendMessage);

        MESSAGE_SENDER.sendMessage(player, MSG_TOP_SELF.getFormattedValue(StringUtil.formatInt(playersPosition)));

        return true;
    }
}