package io.stealingdapenta.mc2048.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class GameCommand  implements CommandExecutor {

    private static final String NOT_CONSOLE = "You can only execute this command directly in the console.";
    private static final String PREFIX_SUFFIX = "=========================================================";
    private static final String COMMAND_EXECUTED = "Clearing console at local time %s.";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ssa dd-MM-yyyy");


    public GameCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            consoleCommandSender.sendMessage(ChatColor.DARK_GRAY + PREFIX_SUFFIX);
            consoleCommandSender.sendMessage(ChatColor.DARK_GREEN + COMMAND_EXECUTED.formatted(LocalDateTime.now()
                                                                                                            .format(formatter)));
            consoleCommandSender.sendMessage(ChatColor.DARK_GRAY + PREFIX_SUFFIX);


            return true;
        }

        sender.sendMessage(ChatColor.RED + NOT_CONSOLE);

        return true;
    }
}