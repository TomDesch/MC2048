package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private static final String GAME_TITLE = "               &4&lMC 2048";
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
        return Bukkit.createInventory(player, REQUIRED_SIZE, ChatColor.translateAlternateColorCodes('&', GAME_TITLE));
    }

    private void fillSides(Inventory inventory) {
        if (inventory.getSize() != REQUIRED_SIZE) {
            logger.warning(WRONG_SIZE);
            return;
        }

        for (int i = 0; i < REQUIRED_SIZE; i++) {
            if (isWithinBorder(i) || !isWithinMiddle(i)) {
                inventory.setItem(i, getStainedGlassPaneItem());
            }
        }
    }

    private ItemStack getStainedGlassPaneItem() {
        return new ItemBuilder(new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1)).setDisplayName(" ")
                                                                                   .create();
    }

    private void setButtonsAndStats(Inventory inventory, Player player) {
        setItemInSlot(inventory, SLOT_UP, createButton("&2&lUP"));
        setItemInSlot(inventory, SLOT_LEFT, createButton("&2&lLEFT"));
        setItemInSlot(inventory, SLOT_RIGHT, createButton("&2&lRIGHT"));
        setItemInSlot(inventory, SLOT_DOWN, createButton("&2&lDOWN"));
        setItemInSlot(inventory, SLOT_STATS, createStatsItem(player));
    }

    private ItemStack createButton(String buttonName) {
        return new ItemBuilder(Material.LIGHTNING_ROD).setDisplayName(buttonName)
                                                      .addLore("&aClick to move everything!")
                                                      .create();
    }

    private ItemStack createStatsItem(Player player) {
        return new ItemBuilder(Material.RECOVERY_COMPASS).setDisplayName("&6&l" + player.getName() + "s stats")
                                                         .addLore("&e>> Playtime: 11:12:13s")
                                                         .addLore("&e>> HiScore: 2048")
                                                         .addLore("&e>> Current score: 1024")
                                                         .create();
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
        return row >= (INVENTORY_ROWS / 2 - 2) && row < (INVENTORY_ROWS / 2 + 2) && col >= 1 && col < 5;
    }

    public boolean isGameWindow(InventoryView inventoryView) {
        return inventoryView.getTitle()
                            .contains(ChatColor.translateAlternateColorCodes('&', GAME_TITLE.strip()));
    }
}
