package io.stealingdapenta.mc2048.utils;


import static io.stealingdapenta.mc2048.config.ConfigKey.AVERAGE_SCORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.CURRENT_PLAYTIME;
import static io.stealingdapenta.mc2048.config.ConfigKey.CURRENT_SCORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAMES_PLAYED;
import static io.stealingdapenta.mc2048.config.ConfigKey.HIGH_SCORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.NUMBER_OF_UNDO;
import static io.stealingdapenta.mc2048.config.ConfigKey.TOTAL_PLAYTIME;
import static io.stealingdapenta.mc2048.utils.FileManager.FILE_MANAGER;
import static io.stealingdapenta.mc2048.utils.InventoryUtil.getPlayerSkullItem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ActiveGame {

    private final Player player;
    private final RepeatingUpdateTask relatedTask;
    private Inventory gameWindow;
    private final long gameOpenTime;
    private int score;
    private final int highScore;
    private final int attempts;
    private final long totalPlayTime;
    private final double averageScore;
    private int undoLastMoveCounter;
    private ItemStack[][] lastPosition;
    private int scoreGainedAfterLastMove = 0;
    private boolean lastMoveUndo = false;

    public ActiveGame(Player player, RepeatingUpdateTask relatedTask) {
        this.player = player;
        this.relatedTask = relatedTask;
        this.score = 0;
        this.gameOpenTime = System.currentTimeMillis();

        this.highScore = FILE_MANAGER.getIntByKey(player, PlayerConfigField.HIGH_SCORE.getKey());
        this.attempts = FILE_MANAGER.getIntByKey(player, PlayerConfigField.ATTEMPTS.getKey());
        this.totalPlayTime = FILE_MANAGER.getLongByKey(player, PlayerConfigField.TOTAL_PLAYTIME.getKey());
        this.averageScore = FILE_MANAGER.getDoubleByKey(player, PlayerConfigField.AVERAGE_SCORE.getKey());
        this.undoLastMoveCounter = NUMBER_OF_UNDO.getIntValue();
    }

    public static String makeSecondsATimestamp(long totalMilliSeconds) {
        int totalSeconds = (int) (totalMilliSeconds / 1000);
        int hours = totalSeconds / 3600;
        int remainder = totalSeconds % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60;
        return hours + "h " + minutes + "m " + seconds + "s";
    }

    public RepeatingUpdateTask getRelatedTask() {
        return relatedTask;
    }

    public Inventory getGameWindow() {
        return gameWindow;
    }

    public Player getPlayer() {
        return player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setGameWindow(Inventory gameWindow) {
        this.gameWindow = gameWindow;
    }

    public void addToScore(int amount) {
        setScore(getScore() + amount);
    }

    public long getMillisecondsSinceStart() {
        return System.currentTimeMillis() - gameOpenTime;
    }

    public String getCurrentPlayTimeFormatted() {
        return makeSecondsATimestamp(getMillisecondsSinceStart());
    }

    public String getTotalPlusCurrentPlayTimeFormatted() {
        return makeSecondsATimestamp(getMillisecondsSinceStart() + getTotalPlayTime());
    }

    public int getHighScore() {
        return highScore;
    }

    public int getAttempts() {
        return attempts;
    }

    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void decrementUndoLastMoveCounter() {
        undoLastMoveCounter--;
    }

    public boolean hasNoUndoLastMoveLeft() {
        return undoLastMoveCounter <= 0;
    }

    public int getUndoLastMoveCounter() {
        return undoLastMoveCounter;
    }

    public ItemStack[][] getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(ItemStack[][] lastPosition) {
        this.lastPosition = lastPosition;
    }

    public int getScoreGainedAfterLastMove() {
        return scoreGainedAfterLastMove;
    }

    public void setScoreGainedAfterLastMove(int scoreGainedAfterLastMove) {
        this.scoreGainedAfterLastMove = scoreGainedAfterLastMove;
    }

    public void resetGainedAfterLastMove() {
        scoreGainedAfterLastMove = 0;
    }

    public void addToGainedAfterLastMove(int amount) {
        setScoreGainedAfterLastMove(getScoreGainedAfterLastMove() + amount);
    }

    public boolean isLastMoveUndo() {
        return lastMoveUndo;
    }

    public void setLastMoveUndo(boolean lastMoveUndo) {
        this.lastMoveUndo = lastMoveUndo;
    }

    public double calculateNewAverageScore() {
        return (getAttempts() * getAverageScore() + getScore()) / (getAttempts() + 1);
    }

    public void updateStatisticsItem() {
        final int SLOT_STATS = 25;
        getGameWindow().setItem(SLOT_STATS, getPlayerStatsHead());
    }

    private ItemStack getPlayerStatsHead() {
        return (new ItemBuilder(getPlayerSkullItem(getPlayer()))).addLore(TOTAL_PLAYTIME.getFormattedValue(getTotalPlusCurrentPlayTimeFormatted()))
                                                                 .addLore(CURRENT_PLAYTIME.getFormattedValue(getCurrentPlayTimeFormatted()))
                                                                 .addLore(HIGH_SCORE.getFormattedValue(String.valueOf(getHighScore())))
                                                                 .addLore(CURRENT_SCORE.getFormattedValue(String.valueOf(getScore())))
                                                                 .addLore(GAMES_PLAYED.getFormattedValue(String.valueOf(getAttempts())))
                                                                 .addLore(AVERAGE_SCORE.getFormattedValue(String.valueOf(Math.round(getAverageScore()))))
                                                                 .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                 .create();
    }
}
