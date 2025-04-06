package io.stealingdapenta.mc2048.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GameHolder implements InventoryHolder {
    private final Player player;

    public GameHolder(Player player) {
        this.player = player;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public Player getPlayer() {
        return player;
    }
}
