package io.stealingdapenta.mc2048;

import static io.stealingdapenta.mc2048.commands.Command.HELP;
import static io.stealingdapenta.mc2048.commands.Command.RELOAD;
import static io.stealingdapenta.mc2048.config.ConfigurationFileManager.CONFIGURATION_FILE_MANAGER;

import io.stealingdapenta.mc2048.commands.Command;
import io.stealingdapenta.mc2048.commands.GameCommand;
import io.stealingdapenta.mc2048.commands.HelpCommand;
import io.stealingdapenta.mc2048.commands.HighScoreCommand;
import io.stealingdapenta.mc2048.commands.ReloadConfigCommand;
import io.stealingdapenta.mc2048.listeners.GameControlsListener;
import io.stealingdapenta.mc2048.utils.HighScoreManager;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import java.util.Objects;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class MC2048 extends JavaPlugin {

    private static final String PLUGIN_ENABLED = "MC2048 plugin enabled.";
    private static final String PLUGIN_DISABLED = "MC2048 plugin disabled.";
    public static Logger logger;
    private static MC2048 instance = null;


    private final HighScoreManager highScoreManager = new HighScoreManager();
    private final InventoryUtil inventoryUtil = new InventoryUtil(highScoreManager);
    private final GameManager gameManager = new GameManager(inventoryUtil);
    private final GameControlsListener gameControlsListener = new GameControlsListener(inventoryUtil, gameManager);
    private final GameCommand gameCommand = new GameCommand(gameManager);
    private final HighScoreCommand highScoreCommand = new HighScoreCommand(highScoreManager);
    private final ReloadConfigCommand reloadConfigCommand = new ReloadConfigCommand();
    private final HelpCommand helpCommand = new HelpCommand(inventoryUtil);

    @Override
    public void onEnable() {
        // todo
        // 1. pause feature: pause the game upon close instead of ending it
        // 2. PAPI support
        // 3. A reward system for reaching a certain score
        // 4. Sounds for successful and unsuccessful moves (customisable sound effects)
        // 5. Opportunity to COMPLETELY change the high score / top / score lines

        instance = this;
        logger = getLogger();

        CONFIGURATION_FILE_MANAGER.loadConfig();

        // Register Commands
        Objects.requireNonNull(getCommand(Command._2048.getCommandName()))
               .setExecutor(gameCommand);
        Objects.requireNonNull(getCommand(Command.TOP.getCommandName()))
               .setExecutor(highScoreCommand);
        Objects.requireNonNull(getCommand(RELOAD.getCommandName()))
               .setExecutor(reloadConfigCommand);
        Objects.requireNonNull(getCommand(HELP.getCommandName()))
               .setExecutor(helpCommand);

        // Register Events
        Bukkit.getPluginManager()
              .registerEvents(gameControlsListener, getInstance());

        logger.info(ChatColor.GOLD + PLUGIN_ENABLED);
    }

    @Override
    public void onDisable() {
        logger.info(ChatColor.GOLD + PLUGIN_DISABLED);
    }

    public static MC2048 getInstance() {
        return instance;
    }
}