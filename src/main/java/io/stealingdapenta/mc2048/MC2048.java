package io.stealingdapenta.mc2048;

import static io.stealingdapenta.mc2048.commands.Command.HELP;
import static io.stealingdapenta.mc2048.commands.Command.RELOAD;
import static io.stealingdapenta.mc2048.config.ConfigurationFileManager.CONFIGURATION_FILE_MANAGER;

import io.stealingdapenta.mc2048.commands.Command;
import io.stealingdapenta.mc2048.commands.GameCommand;
import io.stealingdapenta.mc2048.commands.HelpCommand;
import io.stealingdapenta.mc2048.commands.HighScoreCommand;
import io.stealingdapenta.mc2048.commands.ReloadConfigCommand;
import io.stealingdapenta.mc2048.config.ConfigKey;
import io.stealingdapenta.mc2048.listeners.GameControlsListener;
import io.stealingdapenta.mc2048.persistence.PlayerDataService;
import io.stealingdapenta.mc2048.persistence.PlayerDataStore;
import io.stealingdapenta.mc2048.utils.HighScoreManager;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import io.stealingdapenta.mc2048.utils.MessageSender;
import java.time.Duration;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class MC2048 extends JavaPlugin {

    private static final String PLUGIN_ENABLED = "MC2048 plugin enabled.";
    private static final String PLUGIN_DISABLED = "MC2048 plugin disabled.";

    private HighScoreManager highScoreManager;
    private GameManager gameManager;
    private PlayerDataService playerDataService;
    private MessageSender messageSender;

    @Override
    public void onEnable() {
        CONFIGURATION_FILE_MANAGER.loadConfig(this);
        messageSender = new MessageSender(this);
        playerDataService = new PlayerDataService(new PlayerDataStore(
            getDataFolder().toPath().resolve("mc2048"),
            () -> ConfigKey.SPEED_BUTTON_SPEED_DEFAULT.getIntValue()
        ));
        highScoreManager = new HighScoreManager(getLogger());
        InventoryUtil inventoryUtil = new InventoryUtil(this, highScoreManager);
        gameManager = new GameManager(this, inventoryUtil, highScoreManager, playerDataService, messageSender);
        GameControlsListener gameControlsListener =
            new GameControlsListener(this, inventoryUtil, gameManager, messageSender);
        GameCommand gameCommand = new GameCommand(gameManager, messageSender);
        HighScoreCommand highScoreCommand = new HighScoreCommand(highScoreManager, messageSender);
        ReloadConfigCommand reloadConfigCommand =
            new ReloadConfigCommand(this, gameManager, messageSender);
        HelpCommand helpCommand = new HelpCommand(gameManager, messageSender);
        highScoreManager.refreshAsync(playerDataService);

        Objects.requireNonNull(getCommand(Command._2048.getCommandName()))
               .setExecutor(gameCommand);
        Objects.requireNonNull(getCommand(Command.TOP.getCommandName()))
               .setExecutor(highScoreCommand);
        Objects.requireNonNull(getCommand(RELOAD.getCommandName()))
               .setExecutor(reloadConfigCommand);
        Objects.requireNonNull(getCommand(HELP.getCommandName()))
               .setExecutor(helpCommand);

        Bukkit.getPluginManager()
              .registerEvents(gameControlsListener, this);

        getLogger().info(ChatColor.GOLD + PLUGIN_ENABLED);
    }

    @Override
    public void onDisable() {
        if (gameManager != null) {
            gameManager.saveAllForShutdown();
        }
        if (playerDataService != null) {
            PlayerDataService.ShutdownResult result = playerDataService.shutdown(Duration.ofSeconds(10));
            result.failures().forEach(failure ->
                getLogger().severe("Player data shutdown failure: " + failure.getMessage()));
            if (!result.completed()) {
                getLogger().severe(
                    "Player data shutdown did not complete with "
                        + result.pendingTasks()
                        + " pending tasks.");
            }
        }
        if (messageSender != null) {
            messageSender.close();
        }
        getLogger().info(ChatColor.GOLD + PLUGIN_DISABLED);
    }
}