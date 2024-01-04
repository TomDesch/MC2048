package io.stealingdapenta.mc2048.utils;


import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ActiveGame {

    private final Player player;
    private final Inventory gameWindow;
    private int score;

    public ActiveGame(Player player, Inventory gameWindow) {
        this.player = player;
        this.gameWindow = gameWindow;
        this.score = 0;
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
}
