package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryUtil {

    private static final int INVENTORY_ROWS = 6;
    private static final int INVENTORY_COLUMNS = 9;
    private static final int REQUIRED_SIZE = INVENTORY_ROWS * INVENTORY_COLUMNS;
    private static final String WRONG_SIZE = "Error filling sides of inventory: wrong size!";
    private static final String GAME_TITLE = "                MC 2048";
    private static final int SLOT_UP = 16;
    private static final int SLOT_LEFT = 24;
    private static final int SLOT_RIGHT = 26;
    private static final int SLOT_DOWN = 34;
    private static final int SLOT_STATS = 25;

    public Inventory getGameInventory(Player player) {
        Inventory inventory = createChestInventory(player);
        fillSides(inventory);
        setButtonsAndStats(inventory, player);
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

    private void setButtonsAndStats(Inventory inventory, Player player) {
        setItemInSlot(inventory, SLOT_UP, createButton("UP"));
        setItemInSlot(inventory, SLOT_LEFT, createButton("LEFT"));
        setItemInSlot(inventory, SLOT_RIGHT, createButton("RIGHT"));
        setItemInSlot(inventory, SLOT_DOWN, createButton("DOWN"));
        setItemInSlot(inventory, SLOT_STATS, createStatsItem(player));
    }

    private ItemStack createButton(String buttonName) {
        ItemStack statsItem = new ItemStack(Material.LIGHTNING_ROD);

        ItemMeta itemMeta = statsItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(buttonName);

        List<String> lore = new ArrayList<>();
        lore.add("Click to move everything!");
        itemMeta.setLore(lore);
        statsItem.setItemMeta(itemMeta);
        return statsItem;
    }

    private ItemStack createStatsItem(Player player) {
        ItemStack statsItem = new ItemStack(Material.RECOVERY_COMPASS);

        ItemMeta itemMeta = statsItem.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(player.getName() + "s stats");

        List<String> lore = new ArrayList<>();
        lore.add(">> Playtime: 11:12:13s");
        lore.add(">> HiScore: 2048");
        lore.add(">> Current score: 1024");
        itemMeta.setLore(lore);
        statsItem.setItemMeta(itemMeta);
        return statsItem;
    }

    private void setItemInSlot(Inventory inventory, int slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
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
        return inventoryView.getTitle()
                            .equals(GAME_TITLE);
    }
}
