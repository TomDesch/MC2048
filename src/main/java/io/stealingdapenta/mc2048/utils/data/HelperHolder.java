package io.stealingdapenta.mc2048.utils.data;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public record HelperHolder(Player player) implements InventoryHolder {


    /**
     * @throws UnsupportedOperationException if this is ever called.
     */
    @Override
    public @NotNull Inventory getInventory() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Don't implement me.");
    }
}
