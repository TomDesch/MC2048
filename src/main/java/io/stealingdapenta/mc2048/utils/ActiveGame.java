package io.stealingdapenta.mc2048.utils;


import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ActiveGame {

    private final Player player;

    private final RepeatingUpdateTask relatedTask;
    private Inventory gameWindow;
    private long gameOpenTime;
    private int score;

    public ActiveGame(Player player, Inventory gameWindow, RepeatingUpdateTask relatedTask) {
        this(player, relatedTask);
        this.gameWindow = gameWindow;

    }

    public ActiveGame(Player player, RepeatingUpdateTask relatedTask) {
        this.player = player;
        this.relatedTask = relatedTask;
        this.score = 0;
        gameOpenTime = System.currentTimeMillis();
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

    public String getPlayTimeFormatted() {
        return makeSecondsATimestamp(getMillisecondsSinceStart());
    }
}
