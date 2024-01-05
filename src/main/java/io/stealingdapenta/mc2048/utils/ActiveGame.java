package io.stealingdapenta.mc2048.utils;


import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ActiveGame {

    private final Player player;

    private final RepeatingUpdateTask relatedTask;
    private Inventory gameWindow;
    private final long gameOpenTime;
    private int score;
    private final FileManager fileManager = FileManager.getInstance();
    private int hiScore;
    private int attempts;
    private long totalPlayTime;
    private double averageScore;

    public ActiveGame(Player player, RepeatingUpdateTask relatedTask) {
        this.player = player;
        this.relatedTask = relatedTask;
        this.score = 0;
        this.gameOpenTime = System.currentTimeMillis();

        this.hiScore = fileManager.getIntByKey(player, ConfigField.HISCORE.getKey());
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
}
