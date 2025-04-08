package io.stealingdapenta.mc2048.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public record GameHolder(Player player) implements InventoryHolder {


    /**
     * @throws UnsupportedOperationException if this is ever called.
     */
    @Override
    public @NotNull Inventory getInventory() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Don't implement me.");
    }
}
