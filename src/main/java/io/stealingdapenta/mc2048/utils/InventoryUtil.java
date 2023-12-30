package io.stealingdapenta.mc2048.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryUtil {

    public Inventory openChestInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 4 * 9, "MC 2048");
        return inventory;
    }


}
