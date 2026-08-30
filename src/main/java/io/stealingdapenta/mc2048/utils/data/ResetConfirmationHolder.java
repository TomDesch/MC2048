package io.stealingdapenta.mc2048.utils.data;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public record ResetConfirmationHolder(Player player) implements InventoryHolder {

    @Override
    public @NotNull Inventory getInventory() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Don't implement me.");
    }
}
