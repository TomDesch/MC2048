package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

    private static final int INVENTORY_ROWS = 6;
    private static final int INVENTORY_COLUMNS = 9;
    private static final int REQUIRED_SIZE = INVENTORY_ROWS * INVENTORY_COLUMNS;
    private static final String WRONG_SIZE = "Error filling sides of inventory: wrong size!";
    private static final String GAME_TITLE = "                MC 2048";

    public Inventory getGameInventory(Player player) {
        Inventory inventory = createChestInventory(player);
        fillSides(inventory);
        return inventory;
    }

    private Inventory createChestInventory(Player player) {
        return Bukkit.createInventory(player, REQUIRED_SIZE, GAME_TITLE);
    }

    private void fillSides(Inventory inventory) {
        if (inventory.getSize() != REQUIRED_SIZE) {
            logger.warning(WRONG_SIZE);
            return;
        }

        for (int i = 0; i < REQUIRED_SIZE; i++) {
            if (isWithinBorder(i) || !isWithinMiddle(i)) {
                inventory.setItem(i, new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
            }
        }
    }

    private boolean isWithinBorder(int slot) {
        int row = slot / INVENTORY_COLUMNS;
        // Check if the slot is in the top, bottom, or middle rows
        return row == 0 || row == (INVENTORY_ROWS - 1);
    }

    private boolean isWithinMiddle(int slot) {
        int row = slot / INVENTORY_COLUMNS;
        int col = slot % INVENTORY_COLUMNS;

        // Check if the slot is within the middle 4x6 spots
        return row >= (INVENTORY_ROWS / 2 - 2) && row < (INVENTORY_ROWS / 2 + 2) && col >= 2 && col < 6;
    }

    public boolean isGameWindow(InventoryView inventoryView) {
        return inventoryView.getTitle().equals(GAME_TITLE);
    }
}
