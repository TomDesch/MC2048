package io.stealingdapenta.mc2048.utils;


import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ActiveGame {

    private final Player player;
    private final RepeatingUpdateTask relatedTask;
    private Inventory gameWindow;
    private final long gameOpenTime;
    private int score;
    private final int hiScore;
    private final int attempts;
    private final long totalPlayTime;
    private final double averageScore;
    private int undoLastMoveCounter = 1;
    private ItemStack[][] lastPosition;
    private int scoreGainedAfterLastMove = 0;

    public ActiveGame(Player player, RepeatingUpdateTask relatedTask) {
        this.player = player;
        this.relatedTask = relatedTask;
        this.score = 0;
        this.gameOpenTime = System.currentTimeMillis();

        FileManager fileManager = FileManager.getInstance();
        this.hiScore = fileManager.getIntByKey(player, ConfigField.HIGH_SCORE.getKey());
        this.attempts = fileManager.getIntByKey(player, ConfigField.ATTEMPTS.getKey());
        this.totalPlayTime = fileManager.getLongByKey(player, ConfigField.TOTAL_PLAYTIME.getKey());
        this.averageScore = fileManager.getDoubleByKey(player, ConfigField.AVERAGE_SCORE.getKey());
    }

    private static String makeSecondsATimestamp(long totalMilliSeconds) {
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

    public int getHiScore() {
        return hiScore;
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

}
