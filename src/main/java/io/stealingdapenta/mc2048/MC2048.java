package io.stealingdapenta.mc2048;

import io.stealingdapenta.mc2048.commands.GameCommand;
import io.stealingdapenta.mc2048.commands.HighScoreCommand;
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

    private final InventoryUtil inventoryUtil = new InventoryUtil();
    private final GameManager gameManager = new GameManager(inventoryUtil);
    private final GameControlsListener gameControlsListener = new GameControlsListener(inventoryUtil, gameManager);
    private final GameCommand gameCommand = new GameCommand(inventoryUtil, gameManager);
    private final HighScoreManager highScoreManager = new HighScoreManager();
    private final HighScoreCommand highScoreCommand = new HighScoreCommand(highScoreManager);

    @Override
    public void onEnable() {
        instance = this;
        logger = this.getLogger();

        Objects.requireNonNull(this.getCommand("2048"))
               .setExecutor(gameCommand);

        Objects.requireNonNull(this.getCommand("top2048"))
               .setExecutor(highScoreCommand);

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