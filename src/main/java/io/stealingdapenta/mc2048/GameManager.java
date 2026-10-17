package io.stealingdapenta.mc2048;

import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_GAME_RESUMED;
import static io.stealingdapenta.mc2048.config.ConfigKey.MSG_GAME_STARTED;
import static io.stealingdapenta.mc2048.config.ConfigKey.PLAYER_ITEM_SLOT;
import static io.stealingdapenta.mc2048.utils.MessageSender.MESSAGE_SENDER;

import io.stealingdapenta.mc2048.persistence.PlayerData;
import io.stealingdapenta.mc2048.persistence.PlayerDataService;
import io.stealingdapenta.mc2048.utils.HighScoreManager;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import io.stealingdapenta.mc2048.utils.data.ActiveGame;
import io.stealingdapenta.mc2048.utils.data.RepeatingUpdateTask;
import io.stealingdapenta.mc2048.utils.data.SavedGame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GameManager {

    private static final String ERROR_DEACTIVATING = "Error deactivating game for %s; no active game found.";
    private static final long ONE_SECOND_IN_TICKS = 20L;

    private final Map<UUID, ActiveGame> activeGames = new HashMap<>();
    private final Map<UUID, UUID> activationRequests = new HashMap<>();
    private final MC2048 plugin;
    private final InventoryUtil inventoryUtil;
    private final HighScoreManager highScoreManager;
    private final PlayerDataService playerDataService;
    private final Logger logger;
    private boolean shuttingDown;

    public GameManager(
        MC2048 plugin,
        InventoryUtil inventoryUtil,
        HighScoreManager highScoreManager,
        PlayerDataService playerDataService
    ) {
        this.plugin = plugin;
        this.inventoryUtil = inventoryUtil;
        this.highScoreManager = highScoreManager;
        this.playerDataService = playerDataService;
        this.logger = plugin.getLogger();
    }

    public void activateGame(Player player) {
        ActiveGame existingGame = getActiveGame(player);
        if (existingGame != null) {
            existingGame.setResetConfirmationOpen(false);
            player.openInventory(existingGame.getGameWindow());
            return;
        }

        UUID playerId = player.getUniqueId();
        String playerName = player.getName();
        UUID requestId = UUID.randomUUID();
        activationRequests.put(playerId, requestId);
        playerDataService.load(playerId, playerName).whenComplete((playerData, failure) ->
            runOnPrimary(() -> {
                if (!Objects.equals(activationRequests.get(playerId), requestId)) {
                    return;
                }
                activationRequests.remove(playerId, requestId);
                if (failure != null) {
                    reportFailure("loading player data for " + playerName, failure);
                    return;
                }
                if (!player.isOnline()
                    || !playerId.equals(player.getUniqueId())
                    || !playerName.equals(player.getName())
                    || activeGames.containsKey(playerId)) {
                    return;
                }
                startGame(player, playerData);
            })
        );
    }

    public void openHelp(Player player) {
        UUID playerId = player.getUniqueId();
        String playerName = player.getName();
        playerDataService.load(playerId, playerName).whenComplete((playerData, failure) ->
            runOnPrimary(() -> {
                if (failure != null) {
                    reportFailure("loading player statistics for " + playerName, failure);
                    return;
                }
                if (player.isOnline()
                    && playerId.equals(player.getUniqueId())
                    && playerName.equals(player.getName())) {
                    player.openInventory(inventoryUtil.createHelpInventory(player, playerData));
                }
            })
        );
    }

    private void startGame(Player player, PlayerData playerData) {
        MESSAGE_SENDER.sendMessage(player, playerData.savedGame().isPresent() ? MSG_GAME_RESUMED : MSG_GAME_STARTED);
        ActiveGame activeGame = new ActiveGame(player, createTaskUpdatingPlayerStatItem(player), playerData);
        highScoreManager.recordScore(player, activeGame.getHighScore());
        Inventory gameWindow = inventoryUtil.createGameInventory(activeGame);
        activeGames.put(player.getUniqueId(), activeGame);

        if (playerData.savedGame().isPresent()) {
            inventoryUtil.restoreSavedGame(activeGame, playerData.savedGame().orElseThrow());
        } else {
            inventoryUtil.spawnNewBlock(gameWindow);
            inventoryUtil.spawnNewBlock(gameWindow);
            persistGame(activeGame);
        }
        player.openInventory(gameWindow);
    }

    public void deactivateGameFor(Player player) {
        ActiveGame activeGame = activeGames.get(player.getUniqueId());
        if (activeGame == null) {
            logger.warning(ERROR_DEACTIVATING.formatted(player.getName()));
            return;
        }
        pauseGame(activeGame);
    }

    public void saveOnDeparture(Player player) {
        ActiveGame activeGame = activeGames.get(player.getUniqueId());
        if (activeGame != null) {
            pauseGame(activeGame);
        }
        activationRequests.remove(player.getUniqueId());
    }

    public void pauseGame(ActiveGame activeGame) {
        SavedGame savedGame = inventoryUtil.createSavedGame(activeGame);
        removeActiveGame(activeGame);
        saveGame(activeGame, savedGame);
    }

    public void completeGame(ActiveGame activeGame, Runnable onSuccess) {
        UUID playerId = activeGame.getPlayer().getUniqueId();
        String playerName = activeGame.getPlayer().getName();
        int score = activeGame.getScore();
        long elapsedPlaytime = activeGame.getMillisecondsSinceStart();
        double averageScore = activeGame.calculateNewAverageScore();
        playerDataService.update(
            playerId,
            playerName,
            data -> data.completed(score, elapsedPlaytime, averageScore)
        ).whenComplete((updated, failure) -> runOnPrimary(() -> {
            if (failure != null) {
                reportFailure("saving completed game for " + playerName, failure);
                activeGame.setLock(false);
                activeGame.getPlayer().sendMessage("MC2048 could not save the completed game. Please try again.");
                return;
            }
            if (activeGames.get(playerId) != activeGame) {
                return;
            }
            highScoreManager.recordScore(playerId, playerName, updated.highScore());
            removeActiveGame(activeGame);
            onSuccess.run();
        }));
    }

    public void resetGame(ActiveGame activeGame, Runnable onSuccess) {
        UUID playerId = activeGame.getPlayer().getUniqueId();
        String playerName = activeGame.getPlayer().getName();
        playerDataService.update(playerId, playerName, PlayerData::withoutSavedGame)
                         .whenComplete((ignored, failure) -> runOnPrimary(() -> {
                             if (failure != null) {
                                 reportFailure("resetting game for " + playerName, failure);
                                 activeGame.setLock(false);
                                 activeGame.getPlayer().sendMessage("MC2048 could not reset the game. Please try again.");
                                 activeGame.getPlayer().openInventory(activeGame.getGameWindow());
                                 return;
                             }
                             if (activeGames.get(playerId) != activeGame) {
                                 return;
                             }
                             removeActiveGame(activeGame);
                             onSuccess.run();
                         }));
    }

    public CompletableFuture<PlayerData> persistGame(ActiveGame activeGame) {
        if (activeGames.get(activeGame.getPlayer().getUniqueId()) != activeGame) {
            return CompletableFuture.failedFuture(new IllegalStateException("Game session is no longer active."));
        }
        return saveGame(activeGame, inventoryUtil.createSavedGame(activeGame));
    }

    public void setAnimationSpeed(ActiveGame activeGame, int speed) {
        int previousSpeed = activeGame.getAnimationSpeed();
        activeGame.setAnimationSpeed(speed);
        UUID playerId = activeGame.getPlayer().getUniqueId();
        String playerName = activeGame.getPlayer().getName();
        playerDataService.update(playerId, playerName, data -> data.withAnimationSpeed(speed))
                         .whenComplete((ignored, failure) -> {
                             if (failure == null) {
                                 return;
                             }
                             reportFailure("saving animation speed for " + playerName, failure);
                             runOnPrimary(() -> {
                                 if (activeGames.get(playerId) == activeGame) {
                                     activeGame.setAnimationSpeed(previousSpeed);
                                     inventoryUtil.updateSpeedButton(activeGame, previousSpeed);
                                     activeGame.getPlayer().sendMessage("MC2048 could not save the animation speed.");
                                 }
                             });
                         });
    }

    private CompletableFuture<PlayerData> saveGame(ActiveGame activeGame, SavedGame savedGame) {
        UUID playerId = activeGame.getPlayer().getUniqueId();
        String playerName = activeGame.getPlayer().getName();
        CompletableFuture<PlayerData> save = playerDataService.update(
            playerId, playerName, data -> data.withSavedGame(savedGame));
        save.exceptionally(failure -> {
            reportFailure("saving active game for " + playerName, failure);
            runOnPrimary(() -> {
                if (activeGame.getPlayer().isOnline()
                    && playerId.equals(activeGame.getPlayer().getUniqueId())) {
                    activeGame.getPlayer().sendMessage("MC2048 could not save your game. Please try again.");
                }
            });
            return null;
        });
        return save;
    }

    private void removeActiveGame(ActiveGame activeGame) {
        if (activeGame.getRelatedTask() != null) {
            activeGame.getRelatedTask().cancel();
        }
        activeGames.remove(activeGame.getPlayer().getUniqueId(), activeGame);
    }

    public ActiveGame getActiveGame(Player player) {
        return activeGames.get(player.getUniqueId());
    }

    public boolean hasActiveGames() {
        return !activeGames.isEmpty();
    }

    public void saveAllForShutdown() {
        shuttingDown = true;
        new ArrayList<>(activeGames.values()).forEach(activeGame -> {
            SavedGame savedGame = inventoryUtil.createSavedGame(activeGame);
            removeActiveGame(activeGame);
            saveGame(activeGame, savedGame);
        });
        activationRequests.clear();
    }

    public RepeatingUpdateTask createTaskUpdatingPlayerStatItem(Player player) {
        if (PLAYER_ITEM_SLOT.getIntValue() < 0) {
            return null;
        }

        return new RepeatingUpdateTask(0, ONE_SECOND_IN_TICKS) {
            public void run() {
                ActiveGame activeGame = getActiveGame(player);
                if (activeGame == null) {
                    cancel();
                    return;
                }
                activeGame.updateStatisticsItem();
            }
        };
    }

    private void runOnPrimary(Runnable action) {
        if (!shuttingDown && plugin.isEnabled()) {
            plugin.getServer().getScheduler().runTask(plugin, action);
        }
    }

    private void reportFailure(String operation, Throwable failure) {
        Throwable cause = failure.getCause() == null ? failure : failure.getCause();
        logger.severe("Failed " + operation + ": " + cause.getMessage());
    }
}
